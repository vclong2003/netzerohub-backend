# NetZeroHub Backend

A microservices practice project. NetZeroHub explores a distributed system architecture for sustainability services.
- Web Portal Repository: [NetZeroHub Web](https://github.com/vclong2003/netzerohub-mobile)
- Mobile App Repository: [NetZeroHub App](https://github.com/vclong2003/netzerohub-web)

## Architecture
<img width="3690" height="2415" alt="NetZeroHub Microservices Architecture" src="https://github.com/user-attachments/assets/ecdbd32f-a95f-4d2d-b4af-5f875408547f" />

### Modules
The current backend consists of these modules:

| Module                            | Purpose                                                                                                | Local port |
| --------------------------------- | ------------------------------------------------------------------------------------------------------ | ---------: |
| `config`                          | Spring Cloud Config Server. Loads shared and service-specific configuration from `config/config-repo`. |     `4000` |
| `discovery`                       | Eureka service registry. Services register here and the API Gateway uses it for service discovery.     |     `4001` |
| `apigateway`                      | Single entry point for front-end requests, JWT validation, routing, and OpenAPI aggregation.           |     `4002` |
| `auth`                            | Account registration, verification, login, token refresh, and account operations.                      |     `4003` |
| `notification`                    | Notification processing. Notifications can be sent through email/other providers.                      |     `4004` |
| `storage`                         | File upload and URL generation through the S3-compatible object storage service.                       |     `4005` |
| `common`                          | Shared Java code used by the other modules; it is not a separately running service.                    |          - |
| `services/green_charging_station` | Third-party service for Green Charging Station.                                                        |     `4006` |
| `services/recycling_centre`       | Service for managing recycling centres and their associated waste types.                               |     `4007` |
| `services/pollution_report`       | Service for managing pollution reports and their associated data.                                      |     `4008` |

### Request and authentication flow

1. The front end calls the API Gateway on port `4002`, not the individual
   services.
2. After login, the auth service creates JWT access and refresh tokens.
3. For an authenticated request, the front end sends the access token in the
   following header:

   ```http
   Authorization: Bearer <access-token>
   ```

4. The API Gateway validates the token using the configured public key. It
   extracts user information such as `accountId` and `role`, then forwards the
   request to the selected service with internal headers including
   `X-Account-Id` and `X-Account-Role`.
5. Downstream services read this information through the shared request context.

### Notification service

The notification service consumes notification events and supports notification
delivery through configured providers such as email/OneSignal. The service uses
RabbitMQ on the development server for asynchronous notification processing.

### Storage service

The storage service provides endpoints for the front end to upload files and
obtain signed URLs. The current upload limits are 25 MB per file and
50 MB per request.

## Repository layout

```text
.
├── apigateway/       # API Gateway
├── auth/             # Authentication and account service
├── common/           # Shared library
├── config/           # Config Server
│   └── config-repo/  # Shared and service configuration
├── discovery/        # Eureka service registry
├── notification/     # Notification service
├── services/         # Third-party services
├── storage/          # S3-compatible file storage service
├── compose.yml       # Local backbone services
├── compose.infra.yml # Optional local infrastructure stack
└── Dockerfile        # Common Docker build used by compose.yml
```

## Prerequisites

- Docker and Docker Compose
- Java 25 for running a service directly
- A network connection to the development infrastructure server

The repository includes Maven Wrapper scripts in the root and service folders,
so a separate Maven installation is not required. The Docker build uses Maven
and Eclipse Temurin Java 25.

## Configuration

All application configuration files belong in:

```text
config/config-repo/
```

The Config Server serves these files to the other services. Use the following
conventions:

- `application.properties` contains configuration shared by all services.
- `<service-name>.properties` contains configuration for one service, for
  example `auth.properties` or `storage.properties`.

## Starting the backbone services

From the repository root, build and start all current backbone services with:

```bash
docker compose up -d
```

This command uses [`compose.yml`](compose.yml). Each service is built from the
shared [`Dockerfile`](Dockerfile), with the service module selected through the
Docker build argument. The compose file uses host networking so the services can
communicate through their local ports.

Check container status and logs with:

```bash
docker compose ps
docker compose logs -f <service-name>
```

The compose service names are `config`, `discovery`, `apigateway`, `auth`,
`notification`, and `storage`.

### Rebuilding one backbone service

After changing a backbone service, rebuild and restart with:

```bash
docker compose up -d --build <service-name>
```

For example:

```bash
docker compose up -d --build auth
```

If configuration in `config/config-repo` changes, restart the affected service
so it reloads its configuration. Restarting the full stack is also valid:

```bash
docker compose up -d --build
```

## API and service verification

All front-end API requests should use:

```text
http://localhost:4002
```

Open the gateway Swagger UI at:

<http://localhost:4002/swagger-ui/index.html>

The gateway currently exposes the OpenAPI definitions for the auth and storage
routes. Swagger UI stores authorization in the browser, so use the **Authorize**
button and provide the access token when testing protected endpoints.

The service registry can be viewed at:

<http://localhost:4001>

If Swagger reports that it cannot fetch a service definition, the gateway may
still be refreshing its service registry cache. Wait a few seconds and reload
the Swagger page.

The main route groups currently include:

- `/api/auth/**` and `/api/account/**` - auth and account operations
- `/api/storage/**` - file upload and signed/bucket URL operations

## Running a service directly with Maven

This is useful when developing a new service or when Docker rebuilds are not
needed. First start the backbone services (especially Config Server and
Discovery), then open a terminal in the service directory.

### macOS/Linux

```bash
cd auth
./mvnw clean install
./mvnw spring-boot:run
```

Replace `auth` with the service directory you want to run. The wrapper downloads
the required Maven version automatically if it is not already available.

### Windows PowerShell

```powershell
cd auth
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```

### Windows Command Prompt

```bat
cd auth
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

When running a service directly, make sure another copy of that service is not
already using the same port. The service receives its configuration from the
Config Server at `http://localhost:4000`.

## Adding a third-party service

Services for integrations should be created as separate Spring Boot projects.

1. Generate a project at <https://start.spring.io/>.
2. Select the required Spring Boot version, Java version, and dependencies.
3. Create the project under the repository's `services/` folder, for example:

   ```text
   services/green_charging_station
   ```

4. Add the service's configuration to `config/config-repo`, using a matching
   service name such as `nrma.properties`.
5. Configure the service to import the Config Server and register with Eureka,
   following the existing services as examples.
6. Run it locally with the Maven Wrapper commands above.

## Stopping services

Stop and remove the backbone containers with:

```bash
docker compose down
```
