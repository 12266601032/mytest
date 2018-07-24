package com.example.jsr303demo.validation;

import com.example.jsr303demo.model.ServiceSCMResult;
import com.example.jsr303demo.model.StringCodeMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.core.env.Environment;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Configuration
public class ValidationConfiguration {

    @Bean
    public static MethodValidationPostProcessor methodValidationPostProcessor(
            Environment environment, @Lazy Validator validator,
            @Qualifier("violationToReturnValService")
                    ConversionService conversionService) {
        MethodViolationReturnValPostProcessor processor = new MethodViolationReturnValPostProcessor();
        processor.setConversionService(conversionService);
        processor.setProxyTargetClass(determineProxyTargetClass(environment));
        processor.setValidator(validator);
        return processor;
    }

    @Bean
    public static ConversionService violationToReturnValService() {
        GenericConversionService conversionService = new GenericConversionService();
        conversionService.addConverter(new ViolationToStringCodeMessage());
        return conversionService;
    }

    private static boolean determineProxyTargetClass(Environment environment) {
        RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(environment,
                "spring.aop.");
        Boolean value = resolver.getProperty("proxyTargetClass", Boolean.class);
        return (value != null ? value : true);
    }


    static class ViolationToStringCodeMessage implements Converter<Set<ConstraintViolation<?>>, ServiceSCMResult<?>> {

        @Override
        public ServiceSCMResult<?> convert(Set<ConstraintViolation<?>> source) {
            return ServiceSCMResult.error(StringCodeMessage.ILLEGAL_PARAM_CM.getCode(), source.iterator().next().getMessage());
        }
    }

}
