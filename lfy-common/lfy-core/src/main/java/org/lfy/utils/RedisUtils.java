package org.lfy.utils;

import com.alibaba.fastjson.JSON;
import com.sun.xml.internal.bind.v2.TODO;
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

    //========================common========================

    /**
     * 指定缓存失效时间
     *
     * @param key  String
     * @param time long
     * @return boolean
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
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
     * @param key String
     * @return long
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true:存在 , false:不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
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

    //========================String========================

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
        if (!hasKey(redisKey)) {
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
     * @return boolean
     */
    public <V> boolean set(String cacheName, String key, V v) {
        try {
            redisTemplate.opsForValue().set(redisKey(cacheName, key).toLowerCase(), v);
            return true;
        } catch (Exception e) {
            log.error("RedisUtils-set Error...", e);
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param cacheName String
     * @param key       键
     * @param v         值
     * @param time      时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true：成功 ， false：失败
     */
    public <V> boolean set(String cacheName, String key, V v, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(redisKey(cacheName, key).toLowerCase(), v, time, TimeUnit.SECONDS);
            } else {
                set(cacheName, key, v);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisUtils-set Error...", e);
            return false;
        }
    }


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
