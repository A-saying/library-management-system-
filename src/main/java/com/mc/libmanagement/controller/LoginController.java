package com.mc.libmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mc.libmanagement.reporsitory.domain.User;
import com.mc.libmanagement.reporsitory.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Description: 登录相关的控制器
 * @Author: mingchu
 * @Date: 2025-08-03 20:27
 **/
@Controller
public class LoginController {

    @Resource // 告诉Spring(框架) 自动去对象池中捞取UserService类的对象，注入到LoginController 类中
    private UserService userService;

    @ResponseBody
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "注销成功";
    }



    @RequestMapping({"/"}) // / 其实就表示地址路径只有ip+端口号
    public String portal() {
        // 跳转到登录页面
        return "login";
    }

    // 系统登录之后，可以进入的操作
    @RequestMapping("/index")
    public String index(int id, Model model, HttpServletRequest request) {
        User user = userService.getById(id);
        if (user != null) {
            model.addAttribute("user", user);
        } else {
            return "login";
        }
        // 三元表达式  A ? b : c  如果 A是正确的，则返回 b，否则返回c
        /**
         *  return user.getFlag() == 0 ? "admin_index" : "reader_index";
         *  等价于
         * if (user.getFlag() == 0) {
         *      return "admin_index";
         *  } else {
         *      return "reader_index";
         *  }
         */
        return user.getFlag() == 0 ? "admin_index" : "reader_index";
    }


    // 登录操作 是为了让拦截器不拦截 我们的其他请求
    @RequestMapping({"/login"})
    public String login(User user, Model model, HttpServletRequest request) {
        // 创建一个查询条件封装类的对象， 用于查询User表的
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
        // || 或 && 与 !非
        // 如果账号或者密码没有填写，则没法进行登录校验，则直接返回登录页面
        if (user.getUsername() == null || user.getPassword() == null) {
            // 添加错误信息
            model.addAttribute("msg", "账号/密码必填！");
            // forward:xxx 表示跳转到xxx指定的请求
            // forward:/ 表示跳转到/请求
            return "forward:/";
        }
        // 查询User表中的username 字段的值等于填入的账号
        queryWrapper1.eq("username", user.getUsername());
        // 查询用户信息
        User user1 = this.userService.getOne(queryWrapper1);
        if (user1 != null) {
            // 判断 输入的 密码以及角色类型 和 查出来的这个人的是否一致
            if (user1.getPassword().equals(user.getPassword()) && user1.getFlag().equals(user.getFlag())) {
                // 恭喜你，登录成功，我会把你的信息放到model中，方便前端页面在渲染时候使用
                model.addAttribute("user", user1);
                // 登录成功，把用户信息保存到session中，作为身份, 作为登录的标识,从而通过拦截器的校验
                request.getSession().setAttribute("user", user1);
                // 登录成功，根据flag判断是管理员还是读者 从而去跳转到不同的系统页面
                return user.getFlag() == 0 ? "admin_index" : "reader_index";
            } else {
                // 查询到了用户，但是呢，你输入的密码或者身份类型 和 我在数据库中查到的不一样，你没有经过考验
                model.addAttribute("msg", "密码或身份错误！");
                return "forward:/";
            }
        } else {
            // 如果查询出来的user1 是空的，则走到这部分
            model.addAttribute("msg", "账号不存在！");
            return "forward:/";
        }
    }

//    //跳转到锁屏
//    @RequestMapping("/toLock")
//    public String toLock(int id, Model model, HttpServletRequest request) {
//        User user = userService.getById(id);
//        model.addAttribute("user", user);
//        // 上锁成功，把用户上锁信息保存到session中，作为上锁的标识,用于拦截器的校验
//        request.getSession().setAttribute("lockFlag", true);
//        return "lock";
//    }
//
//    //解锁
//    @RequestMapping("/lock")
//    public String lock(int id, String password, Model model, HttpServletRequest request) {
//        // 通过id查询信息
//        User user = userService.getById(id);
//        model.addAttribute("user", user);
//        // 没有进行user == null 的校验，因为id已经存在了系统中，说明账号一定存在
//        // 只需要判断密码是否一致
//        if (user.getPassword().equals(password)) {
//            // 解锁成功，把上锁表示去除
//            request.getSession().removeAttribute("lockFlag");
//            return user.getFlag() == 0 ? "admin_index" : "reader_index";
//        } else {
//            // 密码输入错误，跳转回到锁屏页面
//            model.addAttribute("msg", "密码输入错误，请重试!");
//            return "lock";
//        }
//    }

}
