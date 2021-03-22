package org.lfy.gateway.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * FallbackController
 *
 * @author lfy
 * @date 2021/3/22
 **/
@RestController
@RequestMapping
public class FallbackController {

    @GetMapping("/fallback")
    public Object fallback() {
        Map<String, Object> result = new HashMap<>(4);
        result.put("data", null);
        result.put("code", 500);
        result.put("message", "Get Request fallback!");
        return result;
    }
}
