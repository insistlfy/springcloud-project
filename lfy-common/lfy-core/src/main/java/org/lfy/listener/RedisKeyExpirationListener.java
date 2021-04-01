package org.lfy.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * RedisKeyExpirationListener
 *
 * @author lfy
 * @date 2021/3/31
 **/
@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {

        String redisKey = message.toString();
        log.info("redis-key expire: 【{}】 ", redisKey);

        //取出自定义业务前缀
        String prefixKey = redisKey.substring(0, redisKey.indexOf(":"));

        if (StringUtils.isBlank(prefixKey)) {
            return;
        }

        //TODO
        log.info("");

    }
}