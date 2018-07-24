package com.example.jsr303demo.validation;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.*;
import java.lang.reflect.Method;
import java.util.Set;

public class MethodViolationReturnValInterceptor implements MethodInterceptor {

    private static Method forExecutablesMethod;

    private static Method validateParametersMethod;

    private static Method validateReturnValueMethod;

    private ConversionService conversionService;

    static {
        try {
            forExecutablesMethod = Validator.class.getMethod("forExecutables");
            Class<?> executableValidatorClass = forExecutablesMethod.getReturnType();
            validateParametersMethod = executableValidatorClass.getMethod(
                    "validateParameters", Object.class, Method.class, Object[].class, Class[].class);
            validateReturnValueMethod = executableValidatorClass.getMethod(
                    "validateReturnValue", Object.class, Method.class, Object.class, Class[].class);
        } catch (Exception ex) {
            // Bean Validation 1.1 ExecutableValidator API not available
        }
    }


    private volatile Validator validator;


    /**
     * Create a new MethodViolationReturnValInterceptor using a default JSR-303 validator underneath.
     */
    public MethodViolationReturnValInterceptor() {
        this(forExecutablesMethod != null ? Validation.buildDefaultValidatorFactory() : null);
    }

    /**
     * Create a new MethodViolationReturnValInterceptor using the given JSR-303 ValidatorFactory.
     *
     * @param validatorFactory the JSR-303 ValidatorFactory to use
     */
    public MethodViolationReturnValInterceptor(ValidatorFactory validatorFactory) {
        this(validatorFactory.getValidator());
    }

    /**
     * Create a new MethodViolationReturnValInterceptor using the given JSR-303 Validator.
     *
     * @param validator the JSR-303 Validator to use
     */
    public MethodViolationReturnValInterceptor(Validator validator) {
        Assert.notNull(validator, "validator");
        this.validator = validator;
    }


    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?>[] groups = determineValidationGroups(invocation);

        if (forExecutablesMethod != null) {
            // Standard Bean Validation 1.1 API
            Object execVal;
            try {
                execVal = ReflectionUtils.invokeMethod(forExecutablesMethod, this.validator);
            } catch (AbstractMethodError err) {
                // Probably an adapter (maybe a lazy-init proxy) without BV 1.1 support
                Validator nativeValidator = this.validator.unwrap(Validator.class);
                execVal = ReflectionUtils.invokeMethod(forExecutablesMethod, nativeValidator);
                // If successful, store native Validator for further use
                this.validator = nativeValidator;
            }

            Method methodToValidate = invocation.getMethod();
            Set<ConstraintViolation<?>> result;

            try {
                result = (Set<ConstraintViolation<?>>) ReflectionUtils.invokeMethod(validateParametersMethod,
                        execVal, invocation.getThis(), methodToValidate, invocation.getArguments(), groups);
            } catch (IllegalArgumentException ex) {
                // Probably a generic type mismatch between interface and impl as reported in SPR-12237 / HV-1011
                // Let's try to find the bridged method on the implementation class...
                methodToValidate = BridgeMethodResolver.findBridgedMethod(
                        ClassUtils.getMostSpecificMethod(invocation.getMethod(), invocation.getThis().getClass()));
                result = (Set<ConstraintViolation<?>>) ReflectionUtils.invokeMethod(validateParametersMethod,
                        execVal, invocation.getThis(), methodToValidate, invocation.getArguments(), groups);
            }
            if (!result.isEmpty()) {
                if (conversionService == null || !conversionService.canConvert(result.getClass(), invocation.getMethod().getReturnType())) {
                    throw new ConstraintViolationException(result);
                }
                return conversionService.convert(result, invocation.getMethod().getReturnType());
            }


            Object returnValue = invocation.proceed();
            result = (Set<ConstraintViolation<?>>) ReflectionUtils.invokeMethod(validateReturnValueMethod,
                    execVal, invocation.getThis(), methodToValidate, returnValue, groups);
            if (!result.isEmpty()) {
                throw new ConstraintViolationException(result);
            }
            return returnValue;
        } else {
            return invocation.proceed();
        }
    }

    /**
     * Determine the validation groups to validate against for the given method invocation.
     * <p>Default are the validation groups as specified in the {@link Validated} annotation
     * on the containing target class of the method.
     *
     * @param invocation the current MethodInvocation
     * @return the applicable validation groups as a Class array
     */
    protected Class<?>[] determineValidationGroups(MethodInvocation invocation) {
        Validated validatedAnn = AnnotationUtils.findAnnotation(invocation.getMethod(), Validated.class);
        if (validatedAnn == null) {
            validatedAnn = AnnotationUtils.findAnnotation(invocation.getThis().getClass(), Validated.class);
        }
        return (validatedAnn != null ? validatedAnn.value() : new Class<?>[0]);
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
}
