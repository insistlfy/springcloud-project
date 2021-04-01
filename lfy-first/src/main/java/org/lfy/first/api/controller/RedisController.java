package org.lfy.first.api.controller;

import io.swagger.annotations.Api;
import org.lfy.config.RedisExpireSpaceConfig;
import org.lfy.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RedisUtils redisUtils;

    @PostMapping("01")
    @Cacheable(cacheNames = RedisExpireSpaceConfig.MINUTE_1, key = "#p0", unless = "#result == null")
    public String test01(@RequestParam("userId") Long userId) {
        return "James";
    }

    @PostMapping("02")
    public String test03(@RequestParam("userId") Long userId) {

        Object obj = redisUtils.get("userId");
        if (null == obj) {
            redisUtils.set("userId", userId, 60);
        }
        return String.valueOf(userId);
    }
}
