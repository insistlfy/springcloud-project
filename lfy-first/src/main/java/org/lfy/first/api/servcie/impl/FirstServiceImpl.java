package org.lfy.first.api.servcie.impl;

import lombok.extern.slf4j.Slf4j;
import org.lfy.first.api.servcie.IFirstService;
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

    @Override
    public String second(Long id) {
        return "Second" + id;
    }
}
