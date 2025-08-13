package com.mc.libmanagement.controller.interceptor;

import com.mc.libmanagement.reporsitory.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Description:
 * @Author: mingchu
 * @Date: 2025-08-03 21:26
 **/
public class AuthInterceptor implements HandlerInterceptor {

    // 重写 操作前 需要处理的方法  => 处理任何请求之前，都要先执行这个方法
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        if (user == null) {
            // 未登录, 跳转到登录页面
            response.sendRedirect("/");
            return false;
        } else {
            // 已登录
            return true;
        }
    }

}
