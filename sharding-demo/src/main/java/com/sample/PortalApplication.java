package com.sample;

import com.example.serialization.SerializationUtils;
import com.sample.serial.DemoBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.scheduling.config.AnnotationDrivenBeanDefinitionParser;

import javax.annotation.Resource;

@SpringBootApplication
public class PortalApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(PortalApplication.class, args);
        DemoBean demoBean = new DemoBean();
        SerializationUtils.serialize(demoBean);
        Object deserialize = SerializationUtils.deserialize();
        System.out.println(PortalApplication.class.getClassLoader());
        System.out.println((DemoBean)deserialize);
        System.out.println(deserialize.getClass().getClassLoader());
        context.getBeanFactory().destroySingletons();
    }

}
