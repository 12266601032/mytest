package com.example.jsr303demo.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.List;
import java.util.Set;

public class PrintUtils {

    public static void printViolationsMessages(ConstraintViolationException cve) {
        printViolationsMessages(cve.getConstraintViolations());
    }

    public static void printViolationsMessages(Set<?> violations) {
        for (ConstraintViolation<?> constraintViolation : (Set<ConstraintViolation<?>>)violations) {
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


    public static void printBindingResult(BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError allError : allErrors) {
                System.out.println(allError.toString());
            }
        }
    }
}
