package com.example.demo.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisDistributedLock extends AbstractDistributedLock {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public boolean lock(String lockKey, String value, long expire, int retryTimes, long sleepMillis) {
        int retry = 0;
        while (true) {
            if (redisTemplate.opsForValue().setIfAbsent(lockKey, value, expire, TimeUnit.MILLISECONDS)) {
                return true;
            } else {
                try {
                    if (retry++ > retryTimes) {
                        // 重试次数超上限
                        log.error("ThreadName:" + Thread.currentThread().getName() + ",获取锁超时。key：" + lockKey);
                        return false;
                    }
                    Thread.sleep(sleepMillis);
                } catch (InterruptedException e) {
                    log.error("线程休眠异常", e);
                }
            }
        }
    }

    @Override
    public boolean releaseLock(String lockKey, String value) {
        String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        RedisScript<Boolean> redisScript = RedisScript.of(script, Boolean.class);
        return (boolean) redisTemplate.execute(redisScript, Collections.singletonList(lockKey), value);
    }
}
