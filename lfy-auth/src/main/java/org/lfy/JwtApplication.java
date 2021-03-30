package org.lfy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * JwtApplication
 *
 * @author lfy
 * @date 2021/3/30
 **/
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class JwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtApplication.class);
        log.info("JwtApplication start successfully...");
    }
}
