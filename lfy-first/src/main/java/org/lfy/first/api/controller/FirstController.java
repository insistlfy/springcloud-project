package org.lfy.first.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.lfy.first.api.servcie.IFirstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * FirstController
 *
 * @author lfy
 * @date 2021/3/18
 **/
@RestController
@RequestMapping("/v1/first/")
@Api(tags = "【FirstController】")
public class FirstController {

    @Autowired
    private IFirstService firstService;

    @ApiOperation("first request")
    @PostMapping("first")
    public String first() {
        return "Hello World!";
    }

    @ApiOperation("second request")
    @GetMapping("second")
    public String second(@RequestParam("id") Long id) {
        return firstService.second(id);
    }
}
