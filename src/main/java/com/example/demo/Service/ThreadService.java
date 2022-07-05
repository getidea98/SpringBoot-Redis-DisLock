package com.example.demo.Service;

import com.example.demo.DemoApplication;
import com.example.demo.lock.RedisDistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
public class ThreadService {

    @Resource
    private RedisDistributedLock redisDistributedLock;

    @Async
    public void increase(CountDownLatch countDownLatch) {
        try {
            // 分布式锁的Key
            String key = "RedisLock";
            for (int i = 0; i < 10000; i++) {
                // 获取锁
                String value = UUID.randomUUID().toString();
                redisDistributedLock.lock(key, value);

                DemoApplication.i++;

                //释放锁
                redisDistributedLock.releaseLock(key, value);
            }
        } finally {
            log.info("ThreadNme:" + Thread.currentThread().getName() + "完成处理");
            countDownLatch.countDown();
        }
    }
}
