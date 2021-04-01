package org.lfy.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.TimeUnit;

/**
 * RedisUtils
 *
 * @author lfy
 * @date 2021/4/1
 **/
@Slf4j
@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 指定缓存失效时间
     *
     * @param key  String
     * @param time long
     * @return boolean
     */
    public boolean expire(String cacheName, String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(redisKey(cacheName, key).toLowerCase(), time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisUtils-expire Error...", e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param cacheName String
     * @param key       String
     * @return long
     */
    public long getExpire(String cacheName, String key) {
        if (StringUtils.isAnyBlank(cacheName, key)) {
            throw new RuntimeException("RedisUtils-hasKey, cacheName or key is null");
        }
        return redisTemplate.getExpire(redisKey(cacheName, key).toLowerCase(), TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param cacheName String
     * @param key       String
     * @return boolean
     */
    public boolean hasKey(String cacheName, String key) {

        if (StringUtils.isAnyBlank(cacheName, key)) {
            throw new RuntimeException("RedisUtils-hasKey, cacheName or key is null");
        }
        try {
            return redisTemplate.hasKey(redisKey(cacheName, key).toLowerCase());
        } catch (Exception e) {
            log.error("RedisUtils-hasKey Error...", e);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public void del(String... key) {
        if (null != key && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key String
     * @return Object
     */
    public <V> V get(String cacheName, String key, Class<V> clazz) {

        if (StringUtils.isAnyBlank(cacheName, key)) {
            throw new RuntimeException("RedisUtils-get, cacheName or key is null");
        }

        String redisKey = redisKey(cacheName, key).toLowerCase();
        if (!hasKey(cacheName, key)) {
            log.error("This redisKey is not exist");
            return null;
        }
        BoundValueOperations<String, V> valueOperations = redisTemplate.boundValueOps(redisKey);
        if (null == valueOperations.get()) {
            return null;
        }
        if (clazz == String.class) {
            return valueOperations.get();
        }
        return JSON.parseObject(String.valueOf(valueOperations.get()), clazz);
    }

    /**
     * 普通缓存放入
     *
     * @param cacheName String
     * @param key       String
     * @param v         Object
     */
    public <V> void set(String cacheName, String key, V v) {

        if (null == v || StringUtils.isAnyBlank(cacheName, key)) {
            throw new RuntimeException("RedisUtils-get, cacheName or key is null");
        }

        BoundValueOperations<String, V> valueOperations =
                redisTemplate.boundValueOps(redisKey(cacheName, key).toLowerCase());
        valueOperations.set(v);

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param cacheName String
     * @param key       键
     * @param v         值
     * @param time      时间(秒) time要大于0 如果time小于等于0 将设置无限期
     */
    public <V> void set(String cacheName, String key, V v, long time) {

        if (time < 0 || null == v || StringUtils.isAnyBlank(cacheName, key)) {
            throw new RuntimeException("RedisUtils-get, cacheName or key is null");
        }

        BoundValueOperations<String, V> valueOperations =
                redisTemplate.boundValueOps(redisKey(cacheName, key).toLowerCase());
        valueOperations.set(v, time, TimeUnit.SECONDS);
    }


    // TODO

    /**
     * 规范生成key
     *
     * @param cacheName String
     * @param redisKey  String
     * @return String
     */
    private String redisKey(String cacheName, String redisKey) {
        return cacheName + "::" + redisKey;
    }
}
