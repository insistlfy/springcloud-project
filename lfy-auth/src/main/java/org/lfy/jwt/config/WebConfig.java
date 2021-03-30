package org.lfy.jwt.config;

import org.lfy.jwt.interceptor.JwtAuthTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * WebConfig
 * 拦截器配置类
 *
 * @author lfy
 * @date 2021/3/30
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private JwtAuthTokenInterceptor jwtAuthTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthTokenInterceptor)
                .addPathPatterns("/**");
    }
}
