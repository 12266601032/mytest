package com.example.jsr303demo;

import com.example.jsr303demo.service.MockUserService;
import org.apache.el.lang.EvaluationContext;
import org.apache.el.lang.ExpressionBuilder;
import org.apache.el.parser.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import javax.el.ExpressionFactory;
import javax.el.StandardELContext;
import javax.el.ValueExpression;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

//mergeMode要指定为 与默认监听器合并模式，否则失去mock化实例 依赖注入等默认监听器提供的功能
@TestExecutionListeners(listeners = {SpringTestListenerDemo.MyListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@RunWith(SpringRunner.class)
@SpringBootTest
@MockBean(MockUserService.class)
public class SpringTestListenerDemo {


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
