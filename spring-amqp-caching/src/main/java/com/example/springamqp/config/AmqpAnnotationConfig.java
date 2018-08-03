package com.example.springamqp.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

import java.util.Map;

@Configuration
@EnableRabbit
public class AmqpAnnotationConfig implements RabbitListenerConfigurer {

    @RabbitListener(queues = {"t1.q1"})
    //sendTo的value可以是表达式、也可以是"exchange/routingkey"
    @SendTo("t1.exchange/t2")
    public String handle(@Payload Map<String, String> map,
                         Message<Map<String, String>> message,
                         @Header(name = "amqp_receivedExchange") String exchange,
                         //自动根据Message中的payload内容利用conversion转换
                         String payload,
                         @Headers Map<?, ?> allHeaders) {
        System.out.println("annotation handle message:" + message);
        System.out.println("all header string:" + allHeaders);
        System.out.println("exchange:" + exchange);
        System.out.println(payload);
        return "111111";
    }

    @RabbitListener(queues = {"t1.q2"})
    public void handle2(@Payload Map<String, String> map,
                          Message<Map<String, String>> message) {
        System.out.println("annotation handle2 message:" + message);
    }


    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        factory.setMessageConverter(jsonMessageConverter);
        return factory;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(myHandlerMethodFactory());
    }

    //自定义转换器
    @Bean
    public DefaultMessageHandlerMethodFactory myHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setConversionService(myConversionService());
        return factory;
    }

    @Bean
    public ConversionService myConversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        conversionService.addConverter(mapToStringConverter());
        return conversionService;
    }


    private MapToStringConverter mapToStringConverter() {
        ObjectMapper mapper = new ObjectMapper();
        return new MapToStringConverter(mapper);
    }

    class MapToStringConverter implements Converter<Map<?, ?>, String> {

        private ObjectMapper mapper;

        public MapToStringConverter(ObjectMapper mapper) {
            this.mapper = mapper;
        }

        @Override
        public String convert(Map<?, ?> source) {
            try {
                return this.mapper.writeValueAsString(source);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
