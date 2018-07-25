package com.example.corsserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan(basePackages = "com.example.corsserver.config")
@SpringBootApplication
public class CorsserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(CorsserverApplication.class, args);
    }
}
