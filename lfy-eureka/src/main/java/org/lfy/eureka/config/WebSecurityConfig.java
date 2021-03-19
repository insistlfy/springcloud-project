package org.lfy.eureka.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * WebSecurityConfig
 * 作用：解决Eureka添加Security权限验证导致服务无法注册到Eureka上
 * 参考链接：https://blog.csdn.net/h11401140334/article/details/103260456
 *
 * @author lfy
 * @date 2021/3/19
 **/
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //close csrf
        http.csrf().disable();
        //open auth
        http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
    }
}
