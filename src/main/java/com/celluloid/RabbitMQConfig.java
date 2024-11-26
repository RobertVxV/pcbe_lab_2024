package com.celluloid;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    public static final String watcherExchange = "watcherExchange";
    public static final String deathQueue = "deathQueue";
    public static final String foodQueue = "foodQueue";
    public static final String deathRoutingKey = "deathRoutingKey";
    public static final String foodRoutingKey = "foodRoutingKey";

    public static final String cupidExchange = "cupidExchange";
    public static final String cupidRoutingKey = "cupidRoutingKey";
    public static final String cupidQueue = "cupidQueue";

    @Bean
    public Queue deathQueue()
    {
        return new Queue(deathQueue);
    }

    @Bean
    public Queue foodQueue()
    {
        return new Queue(foodQueue);
    }

    @Bean
    public DirectExchange directWatcherExchange()
    {
        return new DirectExchange(watcherExchange);
    }

    public Binding bindingDeath()
    {
        return BindingBuilder.bind(deathQueue()).to(directWatcherExchange()).with(deathRoutingKey);
    }

    public Binding bindingFood()
    {
        return BindingBuilder.bind(foodQueue()).to(directWatcherExchange()).with(foodRoutingKey);
    }

    @Bean
    public Queue cupidQueue()
    {
        return new Queue(cupidQueue);
    }

    @Bean
    public DirectExchange directCupidExchange()
    {
        return new DirectExchange(cupidExchange);
    }

    public Binding bindingCupid()
    {
        return BindingBuilder.bind(cupidQueue()).to(directCupidExchange()).with(cupidRoutingKey);
    }
}
