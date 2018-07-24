package com.example.jsr303demo.validation;

import org.aopalliance.aop.Advice;
import org.springframework.core.convert.ConversionService;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validator;

public class MethodViolationReturnValPostProcessor extends MethodValidationPostProcessor {

    protected ConversionService conversionService;

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    protected Advice createMethodValidationAdvice(Validator validator) {
        MethodViolationReturnValInterceptor validationInterceptor = new MethodViolationReturnValInterceptor(validator);
        validationInterceptor.setConversionService(conversionService);
        return validationInterceptor;
    }

}
