package org.lfy.second.client;

import org.lfy.second.client.impl.SecondFeignClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * SecondFeignClient
 *
 * @author lfy
 * @date 2021/3/20
 **/
@FeignClient(value = "lfy-second", path = "second/", fallback = SecondFeignClientImpl.class)
public interface SecondFeignClient {

    /**
     * test1
     *
     * @return String
     */
    @GetMapping("v1/test1")
    String test1();
}
