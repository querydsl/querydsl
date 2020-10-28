package com.querydsl.example.jpms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.querydsl.example.jpms.config", "com.querydsl.example.jpms.service.impl", "com.querydsl.example.jpms.controller"})
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}

