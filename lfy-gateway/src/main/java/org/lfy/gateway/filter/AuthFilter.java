package org.lfy.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lfy.config.RedisExpireSpaceConfig;
import org.lfy.constants.BaseConstants;
import org.lfy.gateway.config.JwtConfig;
import org.lfy.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * AuthFilter
 * 网关鉴权
 *
 * @author lfy
 * @date 2021/3/30
 **/
@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    /**
     * permit url
     */
    private List<String> permitAllUriList = new ArrayList<>();

    /**
     * auth uri
     */
    private List<String> authAllUriList = new ArrayList<>();

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private JwtConfig jwtConfig;

    @PostConstruct
    public void init() {
        this.permitAllUriList = Arrays.asList(jwtConfig.getPermitAll().split(BaseConstants.SPLIT_COMMA));
        this.authAllUriList = Arrays.asList(jwtConfig.getAuthUri().split(BaseConstants.SPLIT_COMMA));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String uri = exchange.getRequest().getURI().getPath();
        log.info("uri : 【{}】", uri);
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //跳过不需要验证的路径
        if (isAllowUri(request)) {
            return chain.filter(exchange);
        }

        String token = exchange.getRequest().getHeaders().getFirst("token");
        // Token为空
        if (StringUtils.isBlank(token)) {
            log.warn("token : 【{}】", token);
            JSONObject message = new JSONObject();
            message.put("code", HttpStatus.UNAUTHORIZED);
            message.put("message", "Unable to get JWT Token");
            byte[] bits = message.toString().getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = response.bufferFactory().wrap(bits);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add("Content-Type", "text/json;charset=UTF-8");
            return response.writeWith(Mono.just(buffer));
        }

        // 校验Token有效性
        if (!jwtConfig.validateToken(token)) {
            throw new RuntimeException("Invalid Token");
        }

        // 解析Token用户信息并设置到Header中
        Claims claims = jwtConfig.getTokenClaim(token);
        String subject = claims.getSubject();
        log.info("AuthFilter-subject :【{}】", subject);
        exchange = exchange.mutate().request(request).build();
        return chain.filter(exchange);
    }

    /**
     * 过滤器执行顺序，数值越小，优先级越高
     *
     * @return int
     */
    @Override
    public int getOrder() {
        return 0;
    }

    private boolean isAllowUri(ServerHttpRequest request) {
        String uri = request.getURI().getPath();
        if (redisUtils.hasKey(RedisExpireSpaceConfig.COMMON_URI, uri)) {
            return redisUtils.get(RedisExpireSpaceConfig.COMMON_URI, uri, Boolean.class);
        }
        boolean flag = checkRequestUri(uri);
        //将数据存入缓存
        redisUtils.set(RedisExpireSpaceConfig.COMMON_URI, uri, flag, TimeUnit.DAYS, 1);
        return flag;
    }

    private boolean checkRequestUri(String uri) {
        AtomicBoolean filter = new AtomicBoolean(true);
        final PathMatcher pathMatcher = new AntPathMatcher();
        permitAllUriList.forEach(e -> {
            if (pathMatcher.match(e, uri)) {
                // permit all的链接直接放行
                filter.set(true);
            }
        });

        authAllUriList.forEach(e -> {
            if (pathMatcher.match(e, uri)) {
                filter.set(false);
            }
        });
        return filter.get();
    }
}
