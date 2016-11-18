//package com.yanbinwa.OASystem.Configure;
//
//import java.util.Arrays;
//
//import javax.jms.ConnectionFactory;
//
//import org.apache.activemq.spring.ActiveMQConnectionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.env.Environment;
//import org.springframework.jms.connection.CachingConnectionFactory;
//import org.springframework.jms.core.JmsTemplate;
//import org.springframework.jms.listener.AbstractPollingMessageListenerContainer;
//import org.springframework.jms.listener.DefaultMessageListenerContainer;
//import org.springframework.jms.support.converter.MessageConverter;
//import org.springframework.jms.support.converter.SimpleMessageConverter;
//
//import com.yanbinwa.OASystem.Service.MessageReceiver;
//
//@Configuration
//@PropertySource(value = { "classpath:application.properties" })
//public class MessagingConfiguration
//{
//    @Autowired
//    private Environment environment;
//    
//    @Autowired
//    MessageReceiver messageReceiver;
//    
//    @Bean
//    public ConnectionFactory connectionFactory(){
//        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
//        connectionFactory.setBrokerURL(environment.getProperty("MessageBrokerUrl"));
//        connectionFactory.setTrustedPackages(Arrays.asList("com.yanbinwa.OASystem"));
//        return connectionFactory;
//    }
//    
//    @Bean
//    public ConnectionFactory cachingConnectionFactory(){
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setTargetConnectionFactory(connectionFactory());
//        connectionFactory.setSessionCacheSize(10);
//        return connectionFactory;
//    }
//    
//    @Bean
//    public AbstractPollingMessageListenerContainer getContainer(){
//        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory());
//        container.setDestinationName(environment.getProperty("OrderQueue"));
//        container.setMessageListener(messageReceiver);
//        return container;
//    }
//
//    @Bean
//    public JmsTemplate jmsTemplate(){
//        JmsTemplate template = new JmsTemplate();
//        template.setConnectionFactory(connectionFactory());
//        template.setDefaultDestinationName(environment.getProperty("OrderResponseQueue"));
//        return template;
//    }
//    
//    @Bean
//    MessageConverter converter(){
//        return new SimpleMessageConverter();
//    }
//}
