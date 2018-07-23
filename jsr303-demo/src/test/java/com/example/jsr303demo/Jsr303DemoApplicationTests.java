package com.example.jsr303demo;

import com.example.jsr303demo.controller.UserController;
import com.example.jsr303demo.service.MockUserService;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@AutoConfigureWebClient(registerRestTemplate = true)
@MockBean(MockUserService.class)
public class Jsr303DemoApplicationTests {

    @Autowired
    MockMvc mvc;
    @Autowired
    RestTemplate restTemplate;
    /**
     * org.springframework.boot.test.mock.mockito.MockitoPostProcessor负责处理该注解
     * 使用方法一：
     * 可以加到测试类上面，允许注解多个
     *
     * @MockBean(MockUserService1.class)
     * @MockBean(MockUserService2.class) 二：
     * 根据注解字段的类型进行mock实例注册
     * @MockBean MockUserService mockUserService;
     * 三：
     * 可以加个测试类上面
     * @MockBeans({
     * @MockBean(MockUserService1.class),
     * @MockBean(MockUserService2.class) })
     */
    @Autowired
    MockUserService mockUserService;



    @Test
    public void contextLoads() throws Exception {
        this.mvc.perform(post("/user")
                .accept("text/html;charset=utf-8")
                .param("username", "abc")
                .param("password", "123456"))
                .andExpect(status().isOk())
                .andExpect(content().string("必须以xx开头"));

    }

    @Test
    public void restTemplate() {
        ResponseEntity<String> exchange = restTemplate
                .exchange(RequestEntity.post(URI.create("https://www.baidu.com"))
                        .build(), String.class);
        System.out.println(exchange.getBody());
    }

    public static void main(String[] args) throws InitializationError {

        JUnitCore jUnitCore = new JUnitCore();
        jUnitCore.addListener(new RunListener(){
            @Override
            public void testRunFinished(Result result) throws Exception {
                System.out.println("测试完毕！");
            }

            @Override
            public void testStarted(Description description) throws Exception {
                System.out.println("测试开始！");
            }
        });
        jUnitCore.run(new SpringRunner(Jsr303DemoApplicationTests.class));

    }
}
