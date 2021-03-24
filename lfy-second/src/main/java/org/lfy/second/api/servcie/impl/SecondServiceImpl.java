package org.lfy.second.api.servcie.impl;

import com.alibaba.fastjson.JSON;
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
    public Object test1(Long time) {
        Object object = firstFeignClient.test3(time);
        log.info("FirstFeignClient --> test3, result : 【{}】。", JSON.toJSONString(object));
        return object;
    }
}
