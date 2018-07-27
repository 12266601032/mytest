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
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
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
        ConstrainedFields fields = new ConstrainedFields(UserController.User.class);
        this.mockMvc
                .perform(put("/user/{id}", "123") //需要使用RestDocumentationRequestBuilders来构造request
                        .header("Authorization", "1")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"username\":\"aaa\"}")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                //index接口文档片段编写
                .andDo(document("user",
                        //请求头列表
                        requestHeaders(
                                headerWithName("Authorization").description(
                                        "Basic auth credentials")),
                        pathParameters(parameterWithName("id").description("用户ID")),
                        //请求参数
                        //requestParameters(),
                        //响应头列表
                        responseHeaders(
                                headerWithName("Content-Type").description(
                                        "The Content Type of response")),
                        //请求字段
                        requestFields(
                                //使用mustache模板语法可以自定义表格列
                                fields.withPath("username").description("用户名"),
                                fields.withPath("password").description("密码").optional().ignored()
                        ),
                        //响应字段
                        responseFields(
                                fieldWithPath("username").description("The user's unique identifier."),
                                fieldWithPath("password").description("The user's password.")
                        )

                ));

    }

    private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), ". ")));
        }
    }
}