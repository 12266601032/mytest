package com.example.jsr303demo.service;

import com.example.jsr303demo.constraints.HasLength;
import com.example.jsr303demo.constraints.IntervalMillis;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Optional;

@Service
@Validated
public class UserService {

    @Autowired
    MockUserService mockUserService;

    @NotNull
    public Boolean updatePassword(@NotNull String username,
                                  @NotNull @Length(min = 6, max = 10) String password,
                                  @HasLength String pwd2) {
        return this.mockUserService.updatePassword(username, password);
    }

    public void setTime(@IntervalMillis(millis = 1000) Date date) {
    }

}
