package com.mc.libmanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: mingchu
 * @Date: 2025-07-18 21:14
 **/

@RestController //  告诉Spring 这是一个处理HTTP请求的类
public class HelloController {

    @GetMapping("/hello") // 处理GET请求，请求路径是 /hello，返回字符串 "Hello, library Builder! Project Setup Success!"
    public String hello() {
        return "Hello, library Builder! Project Setup Success!";
    }

}
