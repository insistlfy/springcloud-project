package org.lfy.jwt.interceptor;

import io.jsonwebtoken.Claims;
import lfy.constants.BaseConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lfy.jwt.config.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * JwtAuthTokenInterceptor
 * 配置Token拦截器
 * ① ：preHandle：调用Controller某个方法之前
 * ② ：postHandle：Controller之后调用，视图渲染之前，如果控制器Controller出现了异常，则不会执行此方法
 * ③ ：afterCompletion：不管有没有异常，这个afterCompletion都会被调用，用于资源清理
 * ④ ：@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次。PostConstruct在构造函数之后执行，init（）方法之前执行。
 * 备注 ： Spring中Constructor、@Autowired、@PostConstruct的顺序
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
    private List<String> permitAllUriList = new ArrayList<>();

    /**
     * auth uri
     */
    private List<String> authAllUriList = new ArrayList<>();

    @Autowired
    private JwtConfig jwtConfig;

    @PostConstruct
    public void init() {
        this.permitAllUriList = Arrays.asList(jwtConfig.getPermitAll().split(BaseConstants.SPLIT_COMMA));
        this.authAllUriList = Arrays.asList(jwtConfig.getAuthUri().split(BaseConstants.SPLIT_COMMA));
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
