package com.example.demo.controller;

import com.example.demo.DemoApplication;
import com.example.demo.Service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;

@RestController
public class RedisController {

    @Autowired
    private ThreadService threadService;

    @GetMapping("/redis")
    public int addToRedis() {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        int i = 0;
        threadService.increase(countDownLatch);
        threadService.increase(countDownLatch);
        try {
            countDownLatch.await();
            i = DemoApplication.i;
            DemoApplication.i = 0;
        } catch (InterruptedException e) {
            return -1;
        }
        return i;
    }
}

