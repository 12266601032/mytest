package com.example.jsr303demo;

import com.example.jsr303demo.utils.PrintUtils;
import org.assertj.core.util.Lists;
import org.hibernate.validator.constraints.Length;
import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.executable.ExecutableValidator;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class BeanValidationTest {

    @Test
    public void bvDemo() {

        //通过spi获取ValidationProvider来构造ValidatorFactory
        ValidatorFactory validatorFactory = Validation.byDefaultProvider()
                .configure()
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        ValidationObject obj = new ValidationObject();
        obj.setUsername(null);
        obj.setPassword("123");
        //验证对象
        Set<ConstraintViolation<ValidationObject>> result = validator.validate(obj);
        PrintUtils.printViolationsMessages(result);
        //ExecutableValidator可以对构造器参数、方法参数、方法返回值上的约束注解进行验证
        ExecutableValidator executableValidator = validator.forExecutables();
    }

    @Test
    public void dataBinderDemo() {
        //构造DataBinder进行参数绑定
        DataBinder dataBinder = new DataBinder(new ValidationObject());
        //添加spring参数验证器
        dataBinder.addValidators(new org.springframework.validation.Validator() {
            @Override
            public boolean supports(Class<?> clazz) {
                return ValidationObject.class.isAssignableFrom(clazz);
            }

            @Override
            public void validate(Object target, Errors errors) {
                ValidationUtils.rejectIfEmpty(errors, "username", "ValidationObject");
                ValidationUtils.rejectIfEmpty(errors, "password", "ValidationObject");
                ValidationObject object = (ValidationObject) target;
                if (object.getPassword().length() < 6 || object.getPassword().length() > 10) {
                    errors.rejectValue("password", "ValidationObject", "密码长度必须为6~10");
                }
            }
        });
        //参数绑定
        dataBinder.bind(new MutablePropertyValues()
                .add("username", "abc")
                .add("password", "123"));
        //触发验证器进行参数验证
        dataBinder.validate();
        BindingResult bindingResult = dataBinder.getBindingResult();
        PrintUtils.printBindingResult(bindingResult);
    }

    @Test
    public void conversionServiceDemo() {
        GenericConversionService conversionService = new DefaultConversionService();

        List<Integer> input = Lists.newArrayList(1, 3, 5);
        List<String> stringList = (List<String>) conversionService.convert(input,
                TypeDescriptor.forObject(input),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(String.class)));
        System.out.println(stringList);
    }

    @Test
    public void formattingConversionServiceDemo() throws NoSuchFieldException {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        MoneyObject bean = new MoneyObject(BigDecimal.ZERO, new Date());
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(bean);
        for (PropertyDescriptor propertyDescriptor : beanWrapper.getPropertyDescriptors()) {
            if ("class".equals(propertyDescriptor.getName())) {
                continue;
            }
            Field field = MoneyObject.class.getDeclaredField(propertyDescriptor.getName());
            Object value = beanWrapper.getPropertyValue(propertyDescriptor.getName());
            String result = (String) conversionService.convert(value, new TypeDescriptor(field), TypeDescriptor.valueOf(String.class));
            System.out.println(result);
        }
    }

    class MoneyObject {

        @NumberFormat(pattern = "0.00")
        private BigDecimal money;
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date date;

        public MoneyObject(BigDecimal money, Date date) {
            this.money = money;
            this.date = date;
        }

        public BigDecimal getMoney() {
            return money;
        }

        public void setMoney(BigDecimal money) {
            this.money = money;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }


    class ValidationObject {

        @NotNull
        private String username;
        @Length(min = 6, max = 10)
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
