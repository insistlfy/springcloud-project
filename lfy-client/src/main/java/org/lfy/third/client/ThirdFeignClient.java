package org.lfy.third.client;

import io.swagger.annotations.ApiOperation;
import org.lfy.third.client.impl.ThirdFeignClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ThirdFeignClient
 * ① ： @FeignClient，name不要和注册中心的服务名称相同，url即为请求地址
 *
 * @author lfy
 * @date 2021/5/8
 **/
@FeignClient(name = "my-feign-client", url = "http://localhost:8888", fallback = ThirdFeignClientImpl.class)
public interface ThirdFeignClient {

    /**
     * test
     *
     * @param str String
     */
    @PostMapping("/v1/retry/test")
    @ApiOperation(value = "test-01")
    void test(@RequestParam("str") String str);
}
