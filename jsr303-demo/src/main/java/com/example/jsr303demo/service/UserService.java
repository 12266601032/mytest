package com.example.jsr303demo.service;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Service
@Validated
public class UserService {

    @Autowired
    MockUserService mockUserService;

    public @NotNull Boolean updatePassword(@NotNull String username, @NotNull @Length(min = 6, max = 10) String password){
        return mockUserService.updatePassword(username, password);
    }
}
