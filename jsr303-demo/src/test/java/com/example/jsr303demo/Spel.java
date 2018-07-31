package com.example.jsr303demo;

import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelNode;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.util.Map;

public class Spel {

    @Test
    public void demo1() throws NoSuchMethodException {
        ExpressionParser parser = new SpelExpressionParser();
        SpelExpression expression = (SpelExpression) parser.parseExpression("{'a': 2+3 }");
        SpelNode ast = expression.getAST();
        System.out.println(expression.toStringAST());
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.registerFunction("replace",
                StringUtils.class.getMethod("replace", String.class, String.class, String.class));
        System.out.println(((Map) expression.getValue()).get("a"));
        System.out.println(parser.parseExpression("#replace('abc','ab','12345')").getValue(context));
    }


}
