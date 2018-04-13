package com.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.type.StandardAnnotationMetadata;

import javax.annotation.Resource;

@EnableAutoConfiguration
public class PortalApplication {

    @Resource
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(PortalApplication.class, args);
    }

}
