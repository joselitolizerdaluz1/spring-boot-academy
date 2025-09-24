package com.example.aoplab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy // TODO 1: Uncomment this line to enable AspectJ auto-proxying
public class AopLabApplication {

    public static void main(String[] args) {
        SpringApplication.run(AopLabApplication.class, args);
    }
}
