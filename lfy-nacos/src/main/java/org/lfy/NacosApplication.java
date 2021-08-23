package org.lfy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * NacosApplication
 *
 * @author lfy
 * @date 2021/8/23
 **/
@Slf4j
@SpringBootApplication
public class NacosApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosApplication.class);
        log.info("NacosApplication start successfully...");
    }
}
