package org.lfy.second.api.servcie.impl;

import lombok.extern.slf4j.Slf4j;
import org.lfy.second.api.servcie.ISecondService;
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

    @Override
    public String test1() {
        return "Hello SecondController...";
    }
}
