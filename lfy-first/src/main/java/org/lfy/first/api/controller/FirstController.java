package org.lfy.first.api.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.lfy.first.api.servcie.IFirstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * FirstController
 *
 * @author lfy
 * @date 2021/3/18
 **/
@RestController
@RequestMapping("/v1/")
@Api(tags = "【FirstController】")
public class FirstController {

    @Autowired
    private IFirstService firstService;

    @ApiOperation("【FirstController --> test1】")
    @HystrixCommand(fallbackMethod = "test1FallBackMethod")
    @PostMapping("test1")
    public String test1() {
        return "Hello World!";
    }

    public String test1FallBackMethod() {
        return null;
    }

    @ApiOperation("【FirstController --> test2】")
    @GetMapping("test2")
    public String test2(@RequestParam("id") Long id) {
        return firstService.second(id);
    }

    @ApiOperation("【FirstController --> test3】")
    @GetMapping("test3")
    public Object test3(@RequestParam("time") Long time) {

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map<String, Object> result = new HashMap<>(4);
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", "Hello World");
        return result;
    }
}
