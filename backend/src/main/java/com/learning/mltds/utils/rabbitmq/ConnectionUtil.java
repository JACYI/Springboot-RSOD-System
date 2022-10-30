package com.learning.mltds.utils.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ConnectionUtil {
    @Value("${spring.rabbitmq.host")
    private static String host = "10.106.128.116";
    @Value("${spring.rabbitmq.virtual-host}")
    private static String virtualHost = "vhost";
    @Value("${spring.rabbitmq.username}")
    private static String username = "yiyonghao";
    @Value("${spring.rabbitmq.password}")
    private static String password = "yiyonghao";

    private static Integer port = 5672;
    /**
     * 建立与RabbitMQ的连接
     * @return
     * @throws Exception
     */
    public static Connection getConnection() throws Exception {
        //定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务地址
        factory.setHost(host);
        //端口
        //    @Value("${spring.rabbitmq.port}")
        factory.setPort(port);
        //设置账号信息，用户名、密码、vhost
        factory.setVirtualHost(virtualHost);//设置虚拟机，一个mq服务可以设置多个虚拟机，每个虚拟机就相当于一个独立的mq
        factory.setUsername(username);
        factory.setPassword(password);
//        System.out.println(factory.getHost());
        // 通过工厂获取连接
        Connection connection = factory.newConnection();
        return connection;
    }

    @Bean
    public static CachingConnectionFactory cachingConnectionFactory(){
        CachingConnectionFactory factory = new CachingConnectionFactory();

        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost(virtualHost);

//        factory.setPublisherConfirms(rabbitProperties.isPublisherConfirms());
//        factory.setPublisherReturns(rabbitProperties.isPublisherReturns());

//        factory.addChannelListener(rabbitChannelListener);
//        factory.addConnectionListener(rabbitConnectionListener);
//        factory.setRecoveryListener(rabbitRecoveryListener);

        return factory;
    }
}
