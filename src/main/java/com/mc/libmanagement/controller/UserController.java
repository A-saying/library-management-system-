package com.mc.libmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mc.libmanagement.reporsitory.domain.User;
import com.mc.libmanagement.reporsitory.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: mingchu
 * @Date: 2025-07-22 21:31
 **/
@Controller  // 告诉Spring(框架)这是一个处理HTTP请求的类
//@RestController  //  ==  @ResponseBody + @Controller 等于这两个注解的合集
public class UserController {

    @Resource  // 自动绑定
    private UserService userService;

    @GetMapping("/get/user")
    @ResponseBody
    public User getUser(@RequestParam("idddd") Integer idhhh) {
        User user = userService.getById(idhhh);
        return user;
    }

    /**
     * 删除用户
     * @param id
     * @return
     *
     * @RequestParam("id")  注解  在请求对象中获取 名字是 “id” 的参数， 更方便，更简单
     *
     */
    @GetMapping("/delete/user")
    @ResponseBody // 注解，告诉Spring 这是一个返回字符串的请求处理方法
    public String deleteUser(@RequestParam("id") Integer id) {
        // 根据用户id 删除用户
        boolean flag = userService.removeById(id);
        if (flag) {
            return "删除成功";
        } else {
            return "删除失败";
        }
    }


    @RequestMapping("/toAddUser")
    public String toAddUser(Model model) {
        User user1 = new User();                  // 创建新用户对象（空表单）
        model.addAttribute("user1", user1);       // 添加空用户对象到模型
        return "html/admin/admin_user_add_1";       // 返回视图模板
    }

    //添加用户
    @ResponseBody
    @PostMapping("/addUser")
    public String addUser(User user,Model model){
        // 我们是不是用了 mybatis-plus 组件 + mybatisX 插件生成了代码，QueryWrapper 是 MybatisPlus 提供的查询条件封装类
        // 创建一个查询条件封装类的对象
        QueryWrapper queryWrapper = new QueryWrapper<>();
        // 查询数据库中 num = 填入的工号 的 记录
        queryWrapper.eq("username", user.getUsername());
        // getOne() 方法是在数据库中查询一条记录
        User one = userService.getOne(queryWrapper);
        if (one == null){
            // one == null 表示 数据库中没有对应num的用户
            userService.save(user);
            User user2 = new User();
            model.addAttribute("user1",user2);
            model.addAttribute("msg","提示:添加成功！");
            return "添加成功";
        } else {
            model.addAttribute("user1",user);
            model.addAttribute("msg","提示:工号重复,添加失败！");
            return  "工号重复,添加失败！";
        }
    }




    @RequestMapping("/toUpdateUser")
    public String toAddUser(int id, Model model) {
        User user = userService.getById(id); // 根据用户id 获取用户
        model.addAttribute("user1", user);     // 添加用户对象到模型
        return "html/admin/admin_user_update_1";       // 返回视图模板
    }

    //添加用户
    @ResponseBody
    @PostMapping("/updateUser")
    public String updateUser(User user,Model model){
        // 我们是不是用了 mybatis-plus 组件 + mybatisX 插件生成了代码，QueryWrapper 是 MybatisPlus 提供的查询条件封装类
        // 创建一个查询条件封装类的对象
        QueryWrapper queryWrapper = new QueryWrapper<>();
        // 查询数据库中 num = 填入的工号 的 记录
        queryWrapper.eq("username", user.getUsername());
        // getOne() 方法是在数据库中查询一条记录
        User one = userService.getOne(queryWrapper);
        if (one == null){
            return "更新失败，该用户不存在，请确认工号填写是否正确";
        } else {
            // 用户不为空，说明用户存在，没问题，可以进行更新
            // 获取记录中的id
            int id = one.getId();
            // 在数据库中更新用户
            user.setId(id);
            // 根据id更新用户
            userService.updateById(user);
            return "更新成功";
        }
    }




}
