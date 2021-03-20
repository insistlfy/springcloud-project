package org.lfy.second.api.servcie.impl;

import lombok.extern.slf4j.Slf4j;
import org.lfy.first.client.FirstFeignClient;
import org.lfy.second.api.servcie.ISecondService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SecondServiceImpl
 *
 * @author lfy
 * @date 2021/3/20
 **/
@Slf4j
@Service
public class SecondServiceImpl implements ISecondService {

    @Autowired
    private FirstFeignClient firstFeignClient;

    @Override
    public String test1() {
        String result = firstFeignClient.test1();
        log.info("FirstFeignClient --> test1, result : 【{}】。", result);
        return result;
    }
}
