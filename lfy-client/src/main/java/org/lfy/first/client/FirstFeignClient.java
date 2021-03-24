package org.lfy.first.client;

import org.lfy.first.client.impl.FirstFeignClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * FirstFeignClient
 *
 * @author lfy
 * @date 2021/3/20
 **/
@FeignClient(value = "lfy-first", path = "first/", fallback = FirstFeignClientImpl.class)
public interface FirstFeignClient {

    /**
     * test1
     *
     * @return String
     */
    @PostMapping("v1/test1")
    String test1();

    /**
     * test3
     *
     * @param time Long
     * @return
     */
    @GetMapping("test3")
    Object test3(@RequestParam("time") Long time);
}
