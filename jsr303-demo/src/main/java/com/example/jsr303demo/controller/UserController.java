package com.example.jsr303demo.controller;

import com.example.jsr303demo.service.MockUserService;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
public class UserController {

    @Autowired
    MockUserService mockUserService;

    @RequestMapping(name = "/user", method = RequestMethod.POST)
    public ResponseEntity<String> register(@Validated({GroupA.class}) User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.ok(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        System.out.println(user);
        return ResponseEntity.ok("ok");
    }


    @InitBinder
    public void initRegisterBinder(DataBinder binder) {
        binder.addValidators(new UserValidator() {
            @Override
            protected void doValidate(User target, Errors errors) {
                if (target == null) {
                    errors.reject("111");
                }
                String username = ((User) target).getUsername();
                ValidationUtils.rejectIfEmpty(errors, "username", "222");
                if(!username.startsWith("xx")){
                    errors.reject("5590", "必须以xx开头");
                }
            }
        });
    }

    public static abstract class UserValidator implements Validator {

        @Override
        public boolean supports(Class<?> clazz) {
            return User.class.isAssignableFrom(clazz);
        }

        @Override
        public void validate(Object target, Errors errors) {
            doValidate((User) target, errors);
        }

        protected abstract void doValidate(User target, Errors errors);
    }

    public static class User {

        @NotNull(groups = {GroupA.class})
        private String username;
        @Length(min = 6, max = 12, groups = {GroupA.class})
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

        @Override
        public String toString() {
            return "User{" +
                    "username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

    public interface GroupA {

    }

    public interface GroupB {

    }

}
