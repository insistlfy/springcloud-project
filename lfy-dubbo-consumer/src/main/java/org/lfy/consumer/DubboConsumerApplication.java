package org.lfy.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * DubboConsumerApplication
 *
 * @author lfy
 * @date 2021/8/23
 **/
@Slf4j
@SpringBootApplication
public class DubboConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DubboConsumerApplication.class);
        log.info("DubboConsumerApplication start successfully...");
    }
}
