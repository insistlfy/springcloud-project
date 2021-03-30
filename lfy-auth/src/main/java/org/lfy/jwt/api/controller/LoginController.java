package org.lfy.jwt.api.controller;

import org.apache.commons.lang3.StringUtils;
import org.lfy.jwt.config.JwtConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * LoginController
 *
 * @author lfy
 * @date 2021/3/30
 **/
@RestController
@RequestMapping
public class LoginController {

    @Resource
    private JwtConfig jwtConfig;

    @PostMapping("/login")
    public Object login(@RequestParam("username") String userName,
                        @RequestParam("password") String password) {

        String token = jwtConfig.generateToken(userName);
        Map<String, Object> result = new HashMap<>(4);
        if (StringUtils.isNotBlank(token)) {
            result.put("token", token);
        }
        result.put("username", userName);
        return result;
    }

    @PostMapping("/api/info")
    public String info() {
        return "Hello Token";
    }
}
