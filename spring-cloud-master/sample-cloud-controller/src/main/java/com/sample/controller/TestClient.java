package com.sample.controller;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("service-test")
public interface TestClient {
	@RequestMapping(method = RequestMethod.GET, value = "/add")
    String getStores();
}
