package com.example.jsr303demo.controller;

import com.example.jsr303demo.service.MockUserService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@AutoConfigureWebClient(registerRestTemplate = true)
@MockBean(classes = MockUserService.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation)).build();
    }

    @Test
    public void register() throws Exception {
        preprocessRequest(prettyPrint());

        this.mockMvc
                .perform(post("/user")
                        .header("Authorization", "1")
                        .accept("text/html;charset=utf-8")
                        .param("username", "abc")
                        .param("password", "123456"))
                .andExpect(status().isOk())
                .andExpect(content().string("必须以xx开头"))
                .andDo(document("index",
                        requestHeaders(
                                headerWithName("Authorization").description(
                                        "Basic auth credentials")),
                        responseHeaders(
                                headerWithName("Content-Type").description(
                                        "The Content Type of response"))));

    }

    @Test
    public void getUser() throws Exception {
        this.mockMvc
                .perform(get("/user/123")
                        .header("Authorization", "1")
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(document("index",
                        requestHeaders(
                                headerWithName("Authorization").description(
                                        "Basic auth credentials")),
                        responseHeaders(
                                headerWithName("Content-Type").description(
                                        "The Content Type of response")),
                        responseFields(
                                fieldWithPath("username").description("The user's unique identifier."),
                                fieldWithPath("password").description("The user's password.")
                        )
                ));

    }
}