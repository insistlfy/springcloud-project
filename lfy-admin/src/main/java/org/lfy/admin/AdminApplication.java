package org.lfy.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * AdminApplication
 * ① ： @EnableAdminServer，开启监控
 *
 * @author lfy
 * @date 2021/3/20
 **/
@Slf4j
@EnableAdminServer
@EnableEurekaClient
@SpringBootApplication
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class);
        log.info("AdminApplication start successfully...");
    }
}
