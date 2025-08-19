package com.mc.libmanagement.controller.config;

import com.mc.libmanagement.controller.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description:
 * @Author: mingchu
 * @Date: 2025-08-03 21:30
 **/

@Configuration  // 表示 这是一个配置类
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                // 添加拦截路径
                .addPathPatterns("/**")
                // 添加不拦截路径
                .excludePathPatterns("/login", "/logout", "/static/**", "/toLogin",
                        "/login.html",
                        "/");
    }
}
