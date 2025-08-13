package com.mc.libmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mc.libmanagement.reporsitory.domain.User;
import org.springframework.ui.Model;
import com.mc.libmanagement.reporsitory.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Description: 登录相关的控制器
 * @Author: mingchu
 * @Date: 2025-08-03 20:27
 **/
@Controller
public class LoginController {

    @Resource  // 自动绑定
    private UserService userService;
//
//
//    // 写一个跳转登录页面的方法
//    @RequestMapping("/toLogin")
//    public String toLogin() {
//        return "login";
//    }


//    @ResponseBody
//    @PostMapping("/login")
//    public String login(User user, Model model, HttpSession session) {
//        if (user == null || user.getUsername() == null || user.getPassword() == null || user.getFlag() == null) {
//            return "用户信息缺失，登录失败";
//        }
//        // 登录界面输入的信息
//        String num = user.getUsername();
//        String password = user.getPassword();
//        Integer role = user.getFlag();
//
//        // 创建一个查询条件封装类的对象
//        QueryWrapper queryWrapper = new QueryWrapper<>();
//        // 查询数据库中 num = 填入的工号 的 记录
//        queryWrapper.eq("username", num);
//        // getOne() 方法是在数据库中查询一条记录
//        User existUser = userService.getOne(queryWrapper);
//        // 1. 判断这个人是否真实存在
//        if (existUser == null) {
//            return "用户不存在，登录失败";
//        }
//        // 2. 判断密码是否正确
//        if (!existUser.getPassword().equals(password)) {
//            return "密码错误，登录失败";
//        }
//        // 3. 判断角色是否正确
//        if (!existUser.getFlag().equals(role)) {
//            return "角色错误，登录失败";
//        }
//        // 4. 登录成功，将用户信息保存到 session 中,作为身份腰牌
//        session.setAttribute("user", existUser);
//        // 登录成功
//        return "登录成功";
//    }

    @ResponseBody
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "注销成功";
    }


    // 优化后：

    @RequestMapping({"/"})
    public String portal() {
        return "login";
    }

    @RequestMapping("/index")
    public String index(int id, Model model, HttpServletRequest request) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
//        request.getSession().setAttribute("LoginSuccess",user);
        return user.getFlag() == 0 ? "admin_index" : "reader_index";
    }

    @RequestMapping({"/login"})
    public String login(User user, Model model, HttpServletRequest request) {
        LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper();
        queryWrapper1.eq(user.getUsername() != null, User::getUsername, user.getUsername());
        User user1 = this.userService.getOne(queryWrapper1);
        if (user1 != null) {
            if (user1.getPassword().equals(user.getPassword()) && user1.getFlag().equals(user.getFlag())) {
                model.addAttribute("user", user1);
                request.getSession().setAttribute("user", user1);
                return user.getFlag() == 0 ? "admin_index" : "reader_index";
            } else {
                model.addAttribute("msg", "密码或身份错误！");
                return "forward:/";
            }
        } else {
            model.addAttribute("msg", "账号不存在！");
            return "forward:/";
        }
    }

    //跳转到锁屏
    @RequestMapping("/toLock")
    public String toLock(int id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "lock";
    }

    //解锁
    @RequestMapping("/lock")
    public String lock(int id, String password, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        if (user.getPassword().equals(password)) {
            return user.getFlag() == 0 ? "admin_index" : "reader_index";
        } else {
            model.addAttribute("msg", "密码输入错误，请重试!");
            return "lock";
        }
    }

}
