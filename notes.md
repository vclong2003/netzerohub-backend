### just a dump note, ignore me

dev server: 146.235.22.194

redis: 3310
rabbitMQ: 3313 (AMQP), 3314 (UI)
rustFS: 3315 (S3 api), 3316 (UI)

config: 4000
discovery: 4001
api-gateway: 4002

auth: 4003
auth-db: 3011

notification: 4004

storage: 4005

green-charging-station: 4006
green-charging-station-db: 3312

recycling-center: 4007
recycling-center-db: 3317

pollution-report: 4008
pollution-report-db: 3318

eureka dashboard: http://localhost:4000
swagger: http://localhost:4002/swagger-ui/index.html

- to do: implement storage files url retrieval in backend
- to do if possible: payment

#### From root run these:

- `mvn clean install -pl common` to re-install common module to cache
- `mvn spring-boot:run -pl <module name>` to run a module, e.g. `mvn spring-boot:run -pl auth`
- If we want to run multiple modules, we can use `mvn spring-boot:run -pl <module1>,<module2>`, e.g. `mvn spring-boot:run -pl auth,notification`

#### Common

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

#### allow infra ports on dev server

```bash
# Sample allow inbound tcp to 443
sudo iptables -I INPUT -p tcp --dport 443 -j ACCEPT
# To allow a range of port, we use --dport 3000:3306
# To allow multiple port, we use --dport 80,443,22


# Persists rules on reboot
sudo netfilter-persistent save

# Liat all tables
sudo iptables -L --line-numbers

# Delete a rule by line number
sudo iptables -D INPUT 1
```

### Mock Charging station

- Around UOW
  ```JSON
  [
    {
        "name": "NRMA Green Station - UOW Northfields Ave P1",
        "latitude": -34.406150,
        "longitude": 150.878720,
        "renewableEnergyPercent": 100
    },
    {
        "name": "NRMA Green Station - UOW Western Carpark P4",
        "latitude": -34.403820,
        "longitude": 150.873210,
        "renewableEnergyPercent": 85
    },
    {
        "name": "NRMA Green Station - UOW Innovation Campus",
        "latitude": -34.398050,
        "longitude": 150.902480,
        "renewableEnergyPercent": 90
    },
    {
        "name": "NRMA Green Station - North Wollongong Station",
        "latitude": -34.413240,
        "longitude": 150.894390,
        "renewableEnergyPercent": 75
    },
    {
        "name": "NRMA Green Station - Wollongong Central",
        "latitude": -34.425120,
        "longitude": 150.893180,
        "renewableEnergyPercent": 60
    }
  ]
  ```
- Sydney Metro

  ```JSON
  [
    {
        "name": "NRMA Green Station - Sydney CBD (Haymarket)",
        "latitude": -33.882950,
        "longitude": 151.206480,
        "renewableEnergyPercent": 50
    },
    {
        "name": "NRMA Green Station - Circular Quay",
        "latitude": -33.856800,
        "longitude": 151.215300,
        "renewableEnergyPercent": 65
    },
    {
        "name": "NRMA Green Station - Sydney Olympic Park",
        "latitude": -33.847520,
        "longitude": 151.063410,
        "renewableEnergyPercent": 95
    },
    {
        "name": "NRMA Green Station - Parramatta Square",
        "latitude": -33.815030,
        "longitude": 151.001140,
        "renewableEnergyPercent": 80
    },
    {
        "name": "NRMA Green Station - Bondi Beach Pavilion",
        "latitude": -33.891470,
        "longitude": 151.276680,
        "renewableEnergyPercent": 70
    }
  ]
  ```

- Regional NSW
  ```JSON
  [
    {
        "name": "NRMA Green Station - Berry (Princes Hwy)",
        "latitude": -34.776100,
        "longitude": 150.697800,
        "renewableEnergyPercent": 100
    },
    {
        "name": "NRMA Green Station - Goulburn (Hume Hwy / Big Merino)",
        "latitude": -34.775820,
        "longitude": 149.694530,
        "renewableEnergyPercent": 85
    },
    {
        "name": "NRMA Green Station - Katoomba (Blue Mountains)",
        "latitude": -33.712610,
        "longitude": 150.311900,
        "renewableEnergyPercent": 90
    },
    {
        "name": "NRMA Green Station - Batemans Bay Foreshore",
        "latitude": -35.707740,
        "longitude": 150.178320,
        "renewableEnergyPercent": 80
    },
    {
        "name": "NRMA Green Station - Newcastle Foreshore",
        "latitude": -32.926710,
        "longitude": 151.776500,
        "renewableEnergyPercent": 75
    },
    {
        "name": "NRMA Green Station - Dubbo Visitor Hub",
        "latitude": -32.247530,
        "longitude": 148.601520,
        "renewableEnergyPercent": 100
    }
  ]
  ```
