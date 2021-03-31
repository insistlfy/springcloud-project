package org.lfy.first.api.controller;

import io.swagger.annotations.Api;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * RedisController
 *
 * @author lfy
 * @date 2021/3/31
 **/
@RestController
@RequestMapping("/v1/")
@Api(tags = "【RedisController】")
public class RedisController {

    @PostMapping("01")
    @Cacheable(key = "#userId")
    public String test01(@RequestParam("userId") Long userId) {
        return "James";
    }
}
