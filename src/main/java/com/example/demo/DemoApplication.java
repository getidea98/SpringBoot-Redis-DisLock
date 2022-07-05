package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DemoApplication {

    public static int i = 0;

    public static void main(String[] c) {

        SpringApplication.run(DemoApplication.class, c);
    }
}
