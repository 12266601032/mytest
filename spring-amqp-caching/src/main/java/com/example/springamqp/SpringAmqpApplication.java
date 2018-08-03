package com.example.springamqp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringAmqpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAmqpApplication.class, args);
    }
}
