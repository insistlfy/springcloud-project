package org.lfy.jwt.interceptor;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lfy.jwt.config.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * JwtAuthTokenInterceptor
 * 配置Token拦截器
 *
 * @author lfy
 * @date 2021/3/29
 **/
@Slf4j
@Component
public class JwtAuthTokenInterceptor extends HandlerInterceptorAdapter {

    /**
     * URL 缓存
     */
    private static final Map<String, Boolean> URI_CACHE_MAP = new ConcurrentHashMap<>();

    /**
     * permit url
     */
    private final List<String> permitAllUriList;

    /**
     * auth uri
     */
    private final List<String> authAllUriList;

//    @Value("${jwt.permit-all}")
//    private String permitAllUri;
//
//    @Value("${jwt.auth-uri}")
//    private String authAllUri;

    @Autowired
    private JwtConfig jwtConfig;

    public JwtAuthTokenInterceptor() {
//        this.permitAllUriList = Arrays.asList(jwtConfig.getPermitAll().split(BaseConstants.SPLIT_COMMA));
//        this.authAllUriList = Arrays.asList(jwtConfig.getAuthUrl().split(BaseConstants.SPLIT_COMMA));
        this.permitAllUriList = new ArrayList<>();
        this.authAllUriList = new ArrayList<>();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (!isAllowUri(request)) {
            final String token = request.getHeader(jwtConfig.getToken());
            log.info("Auth Token :【{}】", token);
            if (StringUtils.isBlank(token)) {
                throw new RuntimeException("Unable to get JWT Token");
            }

            if (!jwtConfig.validateToken(token)) {
                throw new RuntimeException("Invalid Token");
            }

            Claims claims = jwtConfig.getTokenClaim(token);
            request.setAttribute("username", claims.getSubject());
        }
        return true;
    }

    private boolean isAllowUri(HttpServletRequest request) {
        String uri = request.getServletPath();
        if (URI_CACHE_MAP.containsKey(uri)) {
            //缓存中有数据，直接取
            return URI_CACHE_MAP.get(uri);
        }
        boolean flag = checkRequestUri(uri);
        //将数据存入缓存
        URI_CACHE_MAP.put(uri, flag);
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
