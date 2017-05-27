package com.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@Autowired
	TestClient testClient;
	
	@RequestMapping("/say")
    public String home() {
        return testClient.getStores();
    }

}
