package com.sample.client;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SayHelloController {
	
	@RequestMapping(value = "/add" ,method = RequestMethod.GET)
	public String say(){
		return "hello say!";
	}
}
