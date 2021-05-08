package org.lfy.first.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.lfy.third.client.ThirdFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ThirdController
 *
 * @author lfy
 * @date 2021/5/8
 **/
@RestController
@RequestMapping("/v1/api/third")
@Api(tags = "【ThirdController】")
public class ThirdController {

    @Autowired
    private ThirdFeignClient thirdFeignClient;

    @ApiOperation("【FirstController --> test】")
    @GetMapping("test")
    public String test(@RequestParam("str") String str) {
        thirdFeignClient.test(str);
        return "success";
    }

}
