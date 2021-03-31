package lfy.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * RedisConfig
 *
 * @author lfy
 * @date 2021/3/31
 **/
@Slf4j
@Configuration
@EnableCaching
public class RedisConfig extends RedisProperties {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        log.info("Start init Redis...");
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.afterPropertiesSet();
        log.info("Redis init End...");
        return factory;
    }

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        //key都采用String的序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //value的序列化采用FastJson
        redisTemplate.setValueSerializer(new FastJsonRedisSerializer<>(Object.class));
        redisTemplate.setHashValueSerializer(new FastJsonRedisSerializer<>(Object.class));
        //初始化redisTemplate
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 缓存管理器
     *
     * @param factory RedisConnectionFactory
     * @return CacheManager
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {

        //配置默认过期时间和序列化机制
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();

        //自定义缓存空间
        Set<String> cacheNames = new HashSet<>();
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>(RedisExpireSpaceConfig.EXPIRE_TIME_MAP.size());
        for (Map.Entry<String, Integer> entry : RedisExpireSpaceConfig.EXPIRE_TIME_MAP.entrySet()) {
            String cacheName = entry.getKey();
            Integer expireMinute = entry.getValue();
            cacheNames.add(cacheName);
            configMap.put(cacheName, defaultCacheConfig.entryTtl(Duration.ofMinutes(expireMinute)));
        }

        return RedisCacheManager.builder(factory)
                .cacheDefaults(defaultCacheConfig)
                .initialCacheNames(cacheNames)
                .withInitialCacheConfigurations(configMap)
                .build();
    }

    /**
     * 消息监听器
     *
     * @param factory RedisConnectionFactory
     * @return RedisMessageListenerContainer
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory factory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        return container;
    }
}
