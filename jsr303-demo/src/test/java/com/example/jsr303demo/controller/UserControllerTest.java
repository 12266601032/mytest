package com.example.jsr303demo.controller;

import com.example.jsr303demo.service.MockUserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@AutoConfigureWebClient(registerRestTemplate = true)
@MockBean(classes = MockUserService.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
    }

    @Test
    public void register() throws Exception {

    }

}