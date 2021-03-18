package org.lfy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * FirstApplication
 * ① ： @EnableEurekaClient ： 标识EurekaClient
 * @author lfy
 * @date 2021/3/17
 **/
@Slf4j
@EnableEurekaClient
@SpringBootApplication
public class FirstApplication {
    public static void main(String[] args) {
        SpringApplication.run(FirstApplication.class);
        log.info("FirstApplication start successfully...");
    }
}