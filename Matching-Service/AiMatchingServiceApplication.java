package com.lostandfound.aimatching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AiMatchingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiMatchingServiceApplication.class, args);
    }
}