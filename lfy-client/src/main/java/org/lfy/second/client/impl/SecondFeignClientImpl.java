package org.lfy.second.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.lfy.second.client.SecondFeignClient;
import org.springframework.stereotype.Component;

/**
 * SecondFeignClientImpl
 *
 * @author lfy
 * @date 2021/3/20
 **/
@Slf4j
@Component
public class SecondFeignClientImpl implements SecondFeignClient {

    @Override
    public String test1() {
        return "call lfy-second failed,please waiting...";
    }
}
