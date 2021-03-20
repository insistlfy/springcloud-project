package org.lfy.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * AdminApplication
 *
 * @author lfy
 * @date 2021/3/20
 **/
@Slf4j
@EnableEurekaClient
@SpringBootApplication
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class);
        log.info("AdminApplication start successfully...");
    }
}
