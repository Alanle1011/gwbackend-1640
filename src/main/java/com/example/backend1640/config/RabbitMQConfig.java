package com.example.backend1640.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.contribution}")
    private String contributionQueue;

    @Value("${rabbitmq.queue.user}")
    private String userQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.contribution}")
    private String contributionRountingKey;

    @Value("${rabbitmq.routing.user}")
    private String userRountingKey;

    @Bean
    public Queue contributionQueue() {
        return new Queue(contributionQueue);
    }

    @Bean
    public Queue userQueue() {
        return new Queue(userQueue);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding contributionBinding(){
        return BindingBuilder.bind(contributionQueue()).to(exchange()).with(contributionRountingKey);
    }

    @Bean
    public Binding userBinding(){
        return BindingBuilder.bind(userQueue()).to(exchange()).with(userRountingKey);
    }


    @Bean
    MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

}
