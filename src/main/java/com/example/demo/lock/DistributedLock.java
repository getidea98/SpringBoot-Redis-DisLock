package com.example.demo.lock;

public interface DistributedLock {

    // 默认锁过期时间
    public static final long EXPIRE_MILLIS = 3000;

    //默认重试次数
    public static final int RETRY_TIMES = 1000;

    // 默认重试休眠时间
    public static final long SLEEP_MILLIS = 10;

    public boolean lock(String key, String value);

    public boolean lock(String key, String value, int retryTimes);

    public boolean lock(String key, String value, int retryTimes, long sleepMillis);

    public boolean lock(String key, String value, long expire);

    public boolean lock(String key, String value, long expire, int retryTimes);

    public boolean lock(String key, String value, long expire, int retryTimes, long sleepMillis);

    public boolean releaseLock(String key, String value);
}