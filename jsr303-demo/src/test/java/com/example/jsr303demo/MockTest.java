package com.example.jsr303demo;

import com.example.jsr303demo.service.MockUserService;
import com.example.jsr303demo.service.UserService;
import org.apache.el.lang.EvaluationContext;
import org.apache.el.lang.ExpressionBuilder;
import org.apache.el.parser.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.StandardELContext;
import javax.el.ValueExpression;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

//mergeMode要指定为 与默认监听器合并模式，否则失去mock化实例 依赖注入等默认监听器提供的功能
@TestExecutionListeners(listeners = {MockTest.MyListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
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
            userService.updatePassword("abcd", null, "");
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof ConstraintViolationException);
            //printViolationsMessages((ConstraintViolationException) e);
        }


        try {
            userService.updatePassword("abcd", "123", null);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof ConstraintViolationException);
            printViolationsMessages((ConstraintViolationException) e);
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
    public void testIntervalMills(){
        try {
            userService.setTime(new Date(System.currentTimeMillis() - 1500));
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof ConstraintViolationException);
            printViolationsMessages((ConstraintViolationException) e);
        }

        userService.setTime(new Date());
    }

    private void printViolationsMessages(ConstraintViolationException cve) {
        for (ConstraintViolation<?> constraintViolation : cve.getConstraintViolations()) {
            StringBuilder sb = new StringBuilder();
            for (Path.Node node : constraintViolation.getPropertyPath()) {
                switch (node.getKind()) {
                    case BEAN:
                        sb.append("对象：");
                        break;
                    case METHOD:
                        sb.append("方法名称：");
                        break;
                    case PARAMETER:
                        sb.append("参数：");
                        break;
                }
                sb.append(node.getName()).append(",");
            }
            System.out.println(sb.toString() + " message:" + constraintViolation.getMessage());
        }
    }

    @Test
    public void test2() {

        ExpressionFactory expressionFactory = ExpressionFactory.newInstance();
        ValueExpression valueExpression = expressionFactory.createValueExpression(null, Object.class);
        Node node = ExpressionBuilder.createNode("${1+2}");
        StandardELContext standardELContext = new StandardELContext(expressionFactory);
        EvaluationContext evaluationContext = new EvaluationContext(standardELContext, standardELContext.getFunctionMapper(), standardELContext.getVariableMapper());
        Object value = node.getValue(evaluationContext);
        System.out.println(value);
    }

    public static class MyListener extends AbstractTestExecutionListener {

        public MyListener() {
            super();
        }

        @Override
        public void beforeTestClass(TestContext testContext) throws Exception {
            System.out.println("在所有测试类相关生命周期执行前执行");
        }

        @Override
        public void prepareTestInstance(TestContext testContext) throws Exception {
            System.out.println("测试类实例化完毕后，每个测试方法执行前");
        }

        @Override
        public void beforeTestMethod(TestContext testContext) throws Exception {
            System.out.println("每个测试方法执行前");
        }

        @Override
        public void afterTestMethod(TestContext testContext) throws Exception {
            System.out.println("每个测试方法执行后");
        }

        @Override
        public void afterTestClass(TestContext testContext) throws Exception {
            System.out.println("所有测试方法执行后");
        }
    }

}
