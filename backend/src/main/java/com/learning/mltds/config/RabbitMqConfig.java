package com.learning.mltds.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    //交换机名称
    public static final String ITEM_TOPIC_EXCHANGE = "taskExchangeDirect";
    //队列名称
    public static final String ITEM_QUEUE = "taskQueue5";

    //声明交换机
    @Bean("itemTopicExchange")
    public Exchange topicExchange(){
        return ExchangeBuilder.directExchange(ITEM_TOPIC_EXCHANGE).durable(true).build();
//        return ExchangeBuilder.topicExchange(ITEM_TOPIC_EXCHANGE).durable(true).build();
    }

    //声明队列
    @Bean("itemQueue")
    public Queue itemQueue(){
        return QueueBuilder.durable(ITEM_QUEUE).build();
    }

    //绑定队列和交换机
    @Bean
    public Binding itemQueueExchage(@Qualifier("itemQueue")Queue itemQueue,
                                    @Qualifier("itemTopicExchange")Exchange itemTopicExchange){

        return BindingBuilder.bind(itemQueue).to(itemTopicExchange).with("task.111*").noargs();
    }

}