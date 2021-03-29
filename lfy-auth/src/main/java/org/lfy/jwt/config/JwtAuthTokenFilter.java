package org.lfy.jwt.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * JwtAuthTokenFilter
 *
 * @author lfy
 * @date 2021/3/29
 **/
@Slf4j
public class JwtAuthTokenFilter extends OncePerRequestFilter {


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


    @Autowired
    private JwtConfig jwtConfig;

    public JwtAuthTokenFilter() {
        this.permitAllUriList = Arrays.asList(jwtConfig.getPermitAll().split(","));
        this.authAllUriList = Arrays.asList(jwtConfig.getAuthUrl().split(","));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (!isAllowUri(request)) {
            // todo
        }
        chain.doFilter(request, response);
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
