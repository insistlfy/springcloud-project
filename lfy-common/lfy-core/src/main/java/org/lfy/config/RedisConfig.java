package org.lfy.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * RedisConfig
 * ① ：2.X版本可以使用RedisStandaloneConfiguration、RedisSentinelConfiguration、RedisClusterConfiguration三种方式配置连接信息。
 *
 * @author lfy
 * @date 2021/3/31
 **/
@Slf4j
@Setter
@Getter
@Configuration
@EnableCaching
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfig {

    /**
     * Host
     */
    private String host;

    /**
     * password
     */
    private String password;

    /**
     * port
     */
    private Integer port;

    /**
     * 最大连接数
     */
    private Integer maxActive;

    /**
     * 最小空闲连接数
     */
    private Integer minIdle;

    /**
     * 获取连接时的最大等待毫秒数
     */
    private Duration maxWait;

    /**
     * 单节点
     *
     * @return RedisConnectionFactory
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisConnectionFactory redisConnectionFactory() {
        log.info("Start init Redis...");
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPassword(password);
        configuration.setPort(port);
        log.info("Redis init End...");
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder builder =
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
        //配置连接池
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //最大连接数
        jedisPoolConfig.setMaxTotal(maxActive);
        //最小空闲连接数
        jedisPoolConfig.setMinIdle(minIdle);
        //当池内没有可用连接时，最大等待时间
        jedisPoolConfig.setMaxWaitMillis(maxWait.toMillis());
        builder.poolConfig(jedisPoolConfig);
        return new JedisConnectionFactory(configuration, builder.build());
    }

    @Bean
    @ConditionalOnMissingBean
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
    @ConditionalOnMissingBean
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
    @ConditionalOnMissingBean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory factory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        return container;
    }
}
