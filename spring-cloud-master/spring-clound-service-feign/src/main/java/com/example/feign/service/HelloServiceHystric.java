package com.example.feign.service;

import org.springframework.stereotype.Component;

@Component
public class HelloServiceHystric implements HelloService {
    @Override
    public String getSayHiFromClient(String name) {
        return "sorry:" + name;
    }
}
