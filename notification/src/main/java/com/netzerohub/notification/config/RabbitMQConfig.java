package com.netzerohub.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {
    private final RabbitMQProperties rabbitMQProperties;

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(rabbitMQProperties.exchange().notification());
    }

    // Registration ---------------------------------------
    @Bean
    public Queue registrationQueue() {
        return new Queue(rabbitMQProperties.queue().notification().registration(), true);
    }

    @Bean
    public Binding registrationBinding(Queue registrationQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(registrationQueue)
                .to(exchange)
                .with(rabbitMQProperties.routingKey().notification().registration());
    }

    // Password Reset -------------------------------------
    @Bean
    public Queue passwordResetQueue() {
        return new Queue(rabbitMQProperties.queue().notification().passwordReset(), true);
    }

    @Bean
    public Binding passwordResetBinding(Queue passwordResetQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(passwordResetQueue)
                .to(exchange)
                .with(rabbitMQProperties.routingKey().notification().passwordReset());
    }

    // Pollution Report Update ----------------------------
    @Bean
    public Queue pollutionReportUpdateQueue() {
        return new Queue(rabbitMQProperties.queue().notification().pollutionReportUpdate(), true);
    }

    @Bean
    public Binding pollutionReportUpdateBinding(Queue pollutionReportUpdateQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(pollutionReportUpdateQueue)
                .to(exchange)
                .with(rabbitMQProperties.routingKey().notification().pollutionReportUpdate());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

}
