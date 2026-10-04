package com.netzerohub.notification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rabbitmq")
public record RabbitMQProperties(Exchange exchange, Queue queue, RoutingKey routingKey) {
    public record Exchange(String notification) {
    }

    public record Queue(Notification notification) {
        public record Notification(String registration, String passwordReset) {
        }
    }

    public record RoutingKey(Notification notification) {
        public record Notification(String registration, String passwordReset) {
        }
    }
}
