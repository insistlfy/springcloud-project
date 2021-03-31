package org.lfy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * FirstApplication
 * ① ：@EnableEurekaClient，让注册中心发现，并扫描到改服务
 * ② ：@EnableFeignClients，启用Feign客户端
 * ③ ：@EnableHystrix，开启断路器功能，同@EnableCircuitBreaker
 * ④ ：@EnableHystrixDashboard，启用Hystrix Dashboard功能
 *
 * @author lfy
 * @date 2021/3/17
 **/
@Slf4j
@EnableCaching
@EnableEurekaClient
@EnableFeignClients
@EnableHystrix
@EnableHystrixDashboard
@SpringBootApplication
public class FirstApplication {
    public static void main(String[] args) {
        SpringApplication.run(FirstApplication.class);
        log.info("FirstApplication start successfully...");
    }
}
