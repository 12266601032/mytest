package com.example.jsr303demo;

import com.example.jsr303demo.controller.UserController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.properties.PropertyMapping;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@AutoConfigureWebClient(registerRestTemplate = true)
public class Jsr303DemoApplicationTests {

    @Autowired
    MockMvc mvc;
    @Autowired
    RestTemplate restTemplate;

    @Test
    public void contextLoads() throws Exception {
        ResponseEntity<String> exchange = restTemplate
                .exchange(RequestEntity.post(URI.create("https://www.baidu.com"))
                        .build(), String.class);
        System.out.println(exchange.getBody());


        this.mvc.perform(post("/user").param("username", "abc").param("password", "1234"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }
}

