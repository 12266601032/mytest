package com.example.jsr303demo;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.annotation.WebFilter;

@EnableWebMvc
@SpringBootApplication
public class Jsr303DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(Jsr303DemoApplication.class, args);
    }
}
