package com.example.jsr303demo;

import com.example.jsr303demo.service.MockUserService;
import com.example.jsr303demo.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@SpringBootTest
@MockBean(MockUserService.class)
public class MockTest {

    @Autowired
    MockUserService mockUserService;

    @Autowired
    UserService userService;

    @Test
    public void methodValidate() {
        doReturn(null)
                .when(mockUserService)
                .updatePassword(anyString(), anyString());
        try {
            userService.updatePassword("abcd", null);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof ConstraintViolationException);
        }

        try {
            userService.updatePassword(null, "1234567");
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof ConstraintViolationException);
        }

        try {
            userService.updatePassword("adbc", "1234567");
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof ConstraintViolationException);
        }
        doReturn(true)
                .when(mockUserService)
                .updatePassword(anyString(), anyString());
        assertTrue(userService.updatePassword("adbc", "1234567"));
    }

}
