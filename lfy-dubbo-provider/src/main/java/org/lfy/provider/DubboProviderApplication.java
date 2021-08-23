package org.lfy.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * DubboProviderApplication
 *
 * @author lfy
 * @date 2021/8/23
 **/
@Slf4j
@SpringBootApplication
public class DubboProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(DubboProviderApplication.class);
        log.info("DubboProviderApplication start successfully...");
    }
}
