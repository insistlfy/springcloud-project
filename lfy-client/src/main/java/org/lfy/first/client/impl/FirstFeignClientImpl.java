package org.lfy.first.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.lfy.first.client.FirstFeignClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * FirstFeignClientImpl
 *
 * @author lfy
 * @date 2021/3/20
 **/
@Slf4j
@Component
public class FirstFeignClientImpl implements FirstFeignClient {

    @Override
    public String test1() {
        return "call lfy-first failed,please waiting...";
    }

    @Override
    public Object test3(Long time) {
        Map<String, Object> result = new HashMap<>(4);
        result.put("data", null);
        result.put("code", 500);
        result.put("message", "Feign Request fallback!");
        return result;
    }
}
