package com.example.jsr303demo.controller;

import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
public class UserController {

    @RequestMapping(name = "/user", method = RequestMethod.POST)
    public ResponseEntity<String> register(@Validated({GroupA.class}) User user, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.ok(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return ResponseEntity.ok("ok");
    }


    public static class User{

        @NotNull(groups = {GroupA.class})
        private String username;
        @Length(min = 6, max = 12,groups = {GroupA.class})
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

    public interface GroupA{

    }
    public interface GroupB{

    }

}
