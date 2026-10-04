dev server: 146.235.22.194

redis: 3310
rabbitMQ: 3313 (AMQP), 3314 (UI)
rustFS: 4053, 4054

config: 4000
discovery: 4001
api-gateway: 4002

auth: 4003
auth-db: 3011

notification: 4004
notification-db: 3012

storage: 4005

eureka dashboard: http://localhost:4000

### From root run these:

- `mvn clean install -pl common` to re-install common module to cache
- `mvn spring-boot:run -pl <module name>` to run a module, e.g. `mvn spring-boot:run -pl auth`
- If we want to run multiple modules, we can use `mvn spring-boot:run -pl <module1>,<module2>`, e.g. `mvn spring-boot:run -pl auth,notification`

##### Notes

- added resources/META-INF in common config to auto load modules

#### Redis

```bash
docker exec -it NetZeroHub-Redis redis-cli
AUTH <password>
KEYS *
```

#### RabbitMQ

```bash
# create a new user
rabbitmqctl add_user netzerohub your_secure_password

# grant full permissions
rabbitmqctl set_permissions -p "/" netzerohub ".*" ".*" ".*"

# set this tag to access ui if needed
rabbitmqctl set_user_tags netzerohub management
```
