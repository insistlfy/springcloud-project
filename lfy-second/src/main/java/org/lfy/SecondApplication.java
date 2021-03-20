package org.lfy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * SecondApplication
 *
 * @author lfy
 * @date 2021/3/17
 **/
@Slf4j
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
public class SecondApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecondApplication.class);
        log.info("SecondApplication start successfully...");
    }
}
