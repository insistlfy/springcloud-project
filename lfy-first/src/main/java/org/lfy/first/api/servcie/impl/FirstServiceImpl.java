package org.lfy.first.api.servcie.impl;

import lombok.extern.slf4j.Slf4j;
import org.lfy.first.api.servcie.IFirstService;
import org.lfy.second.client.SecondFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * FirstServiceImpl
 *
 * @author lfy
 * @date 2021/3/18
 **/
@Slf4j
@Service
public class FirstServiceImpl implements IFirstService {

    @Autowired
    private SecondFeignClient secondFeignClient;

    @Override
    public String second(Long id) {
        String result = secondFeignClient.test1();
        return result + id;
    }
}
