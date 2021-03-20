package org.lfy.second.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.lfy.second.api.servcie.ISecondService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SecondController
 *
 * @author lfy
 * @date 2021/3/19
 **/
@RestController
@RequestMapping("/v1/")
@Api(tags = "【SecondController】")
public class SecondController {

    @Autowired
    private ISecondService secondService;

    @ApiOperation("【SecondController --> test1】")
    @GetMapping("test1")
    public String test1() {
        return secondService.test1();
    }
}
