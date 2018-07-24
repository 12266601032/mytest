package com.example.jsr303demo;

import com.example.jsr303demo.utils.PrintUtils;
import org.hibernate.validator.constraints.Length;
import org.junit.Test;
import org.springframework.beans.MutablePropertyValues;
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
                if(object.getPassword().length() < 6 || object.getPassword().length() > 10){
                    errors.rejectValue("password","ValidationObject", "密码长度必须为6~10");
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
