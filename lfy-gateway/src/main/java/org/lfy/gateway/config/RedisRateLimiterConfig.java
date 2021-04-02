package org.lfy.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

/**
 * RedisRateLimiterConfig
 * 限流策略的配置类
 * ① userKeyResolver ： 根据请求参数中的username进行限流
 * ② ipKeyResolver ： 根据访问IP进行限流
 * ③ ipKeyResolver ： 根据访问URI进行限流
 *
 * @author lfy
 * @date 2021/3/24
 **/
@Configuration
public class RedisRateLimiterConfig {

    @Primary
    @Bean(name = "ipKeyResolver")
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }

    @Bean("uriKeyResolver")
    public KeyResolver uriKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getURI().getPath());
    }

    @Bean("userKeyResolver")
    KeyResolver userKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("userId"));
    }
}
