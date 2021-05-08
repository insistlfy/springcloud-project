package org.lfy.third.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.lfy.third.client.ThirdFeignClient;
import org.springframework.stereotype.Component;

/**
 * ThirdFeignClientImpl
 *
 * @author lfy
 * @date 2021/5/8
 **/
@Slf4j
@Component
public class ThirdFeignClientImpl implements ThirdFeignClient {

    @Override
    public void test(String str) {

        log.error("call third api failed...");
    }
}
