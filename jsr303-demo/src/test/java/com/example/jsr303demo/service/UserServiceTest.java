package com.example.jsr303demo.service;

import com.example.jsr303demo.model.ServiceSCMResult;
import com.example.jsr303demo.utils.PrintUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@SpringBootTest
@MockBean(MockUserService.class)
public class UserServiceTest {

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
            userService.updatePassword("abcd", null, "");
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof ConstraintViolationException);
        }


        try {
            userService.updatePassword("abcd", "123", null);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof ConstraintViolationException);
            PrintUtils.printViolationsMessages((ConstraintViolationException) e);
        }


        try {
            userService.updatePassword(null, "1234567", "abc");
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof ConstraintViolationException);
        }

        try {
            userService.updatePassword("adbc", "1234567", "abc");
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof ConstraintViolationException);
        }

        doReturn(true)
                .when(mockUserService)
                .updatePassword(anyString(), anyString());
        assertTrue(userService.updatePassword("adbc", "1234567", "abc"));
    }


    @Test
    public void testIntervalMills() {
        try {
            userService.setTime(new Date(System.currentTimeMillis() - 1500));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof ConstraintViolationException);
            PrintUtils.printViolationsMessages((ConstraintViolationException) e);
        }

        userService.setTime(new Date());
    }

    @Test
    public void testIntervalMills2() {
        ServiceSCMResult<String> serviceSCMResult = userService.setTime2(new Date(System.currentTimeMillis() - 1500));
        System.out.println(serviceSCMResult);
    }


}