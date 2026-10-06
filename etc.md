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
notification-db: 3012

storage: 4005

eureka dashboard: http://localhost:4000
swagger: http://localhost:4002/swagger-ui/index.html

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

 <!-- test action -->
 <!-- test action 1 -->
