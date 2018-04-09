package com.sample;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class PortalApplication {

    public static void main(String[] args) throws Exception {
        //ConfigurableApplicationContext context = SpringApplication.run(PortalApplication.class, args);
        GroovyShell loader = new GroovyShell();
        loader.evaluate("print(\"hello\")");
    }
}
