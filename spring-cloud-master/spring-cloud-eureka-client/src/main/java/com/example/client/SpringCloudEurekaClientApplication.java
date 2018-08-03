package com.example.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableEurekaClient
@SpringBootApplication
@EnableBinding(Processor.class)
public class SpringCloudEurekaClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudEurekaClientApplication.class, args);
    }

    @Value("${server.port}")
    private String port;

    @Value("${foo}")
    private String foo;

    private String currentMessage;

    @Autowired
    private Source source;

    @RequestMapping("/hi")
    public String hi(@RequestParam String name) {
        return "hi " + name + ",i am from prot:" + port + ", foo variable:" + foo;
    }

    @RequestMapping("/publish")
    public void publish(@RequestParam String message){

        source.output().send(MessageBuilder.withPayload(message).build());
    }

    @RequestMapping("/currentMessage")
    public String getMessage(){
        return this.currentMessage;
    }



    @StreamListener(Sink.INPUT)
    public void processMessage(String message) {
        this.currentMessage = message;
    }

}
