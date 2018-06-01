package com.example.feign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-hi", fallback = HelloServiceHystric.class)
public interface HelloService {

    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    String getSayHiFromClient(@RequestParam("name") String name);
}
