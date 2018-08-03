package com.example.springamqp.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

@Configuration
public class AmqpConfiguration {

    @Bean
    public ConnectionFactory connectionFactory() {
        //连接配置
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMessageConverter(jsonMessageConverter());
        configureRabbitTemplate(template);
        //设置publisher端设置异步confirm
        template.setConfirmCallback((correlationData, ack, cause) -> {
            if(ack){
                System.out.println("消息发送成功：" + correlationData);
                correlationSet.remove(correlationData.getId());
            }else {
                System.out.println("消息发送失败：" + correlationData);
            }
        });
        return template;
    }


    @Bean
    public Exchange topicExchange() {
        return ExchangeBuilder
                .topicExchange("t1.exchange")
                .autoDelete()
                .durable(false)
                .build();
    }


    protected void configureRabbitTemplate(RabbitTemplate template) {

    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public Queue q1() {
        return QueueBuilder
                .nonDurable("t1.q1")
                .build();
    }

    @Bean
    public Queue q2() {
        return QueueBuilder
                .nonDurable("t1.q2")
                .build();
    }

    @Bean
    public Queue q3() {
        return QueueBuilder
                .nonDurable("t1.q3")
                .build();
    }

    @Bean
    public Queue q4() {
        return new AnonymousQueue(new AnonymousQueue.Base64UrlNamingStrategy("foo-"));
    }


    @Bean
    public Binding b1() {
        return BindingBuilder
                .bind(q1())
                .to(topicExchange())
                .with("t1.*")
                .noargs();

    }

    @Bean
    public Binding b2() {
        return BindingBuilder
                .bind(q4())
                .to(topicExchange())
                .with("t1.*")
                .noargs();

    }

    /**
     * 其中queue与exchange的bindings的声明由RabbitAdmin处理
     */
    @Bean
    public List<Binding> bindings() {
        //集合方式提供binding
        return Arrays.asList(
                new Binding("t1.q2", Binding.DestinationType.QUEUE, "t1.exchange", "t1.*", null),
                new Binding("t1.q3", Binding.DestinationType.QUEUE, "t1.exchange", "t1.*", null)
        );

    }


    /**
     * 队列消费者
     * 1.基于MessageListenerContainer来实现
     * 2.基于注解
     */
    @Bean
    public SimpleMessageListenerContainer messageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        //注意：虽然这里直接是内部方法调用，但是spring通过cglib代理，对@Bean的方法直接从BeanFacotry中返回单例对象
        container.setConnectionFactory(connectionFactory());
        container.addQueues(q4());
        //container实现了容器的Lifecycle，在start时与通道建立绑定关系
        container.setMessageListener(exampleListener());
        return container;
    }

    @Bean
    public MessageListener exampleListener() {
        return message -> System.out.println("received: " + message);
    }

    Set<String> correlationSet = new ConcurrentSkipListSet<>();

    @Scheduled(cron = "0/5 * * * * ?")
    public void sendMessageTask() {
        RabbitTemplate rabbitTemplate = rabbitTemplate();
        ExpressionParser parser = new SpelExpressionParser();
        //利用template中配置的messageConverter来转换对象为Message
        /*rabbitTemplate.convertAndSend(
                b1().getExchange(),
                "t1.*",
                parser.parseExpression("{'a': 'c'}").getValue());*/
        Map<String, String> map = new HashMap<>();
        map.put("a","c");
        String relateId = UUID.randomUUID().toString();
        correlationSet.add(relateId);
        rabbitTemplate.convertAndSend(
                b1().getExchange(),
                "t1.*",
                map,
                new CorrelationData(relateId));
    }


}
