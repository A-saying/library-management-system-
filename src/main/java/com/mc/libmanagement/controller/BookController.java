package com.mc.libmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mc.libmanagement.reporsitory.domain.BookInfo;
import com.mc.libmanagement.reporsitory.domain.ClassInfo;
import com.mc.libmanagement.reporsitory.domain.User;
import com.mc.libmanagement.reporsitory.service.BookInfoService;
import com.mc.libmanagement.reporsitory.service.ClassInfoService;
import com.mc.libmanagement.reporsitory.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 图书相关的控制器操作
 * @Author: mingchu
 * @Date: 2025-08-09 17:47
 **/
@Controller
@RequestMapping("/book")
public class BookController {



    /**
     * 增： 增加一本图书   管理员
     * 删： 删除一本图书   管理员
     * 改：改变图书信息
     *     对图书信息的维护   管理员
     *     借书：图书的数量 -1   读者
     *     还书：图书的数量 +1   读者
     * 查：查询图书列表   管理员、读者
     *      查询全部的图书（分页查询）
     *      查询指定图书（根据名字查询、更加id查询）
     *      查询指定作者的图书（分页查询）
     *    查询图书详细信息
     */
    /**
     * 维护class信息
     * 增、删、查
     */


    @Resource
    private BookInfoService bookInfoService;
    @Resource
    private ClassInfoService classInfoService;
    @Resource
    private UserService userService;

    /**
     * 对图书的操作 管理员 和 读者 跳转的页面不一样
     */

    //分页查询全部
    @RequestMapping("/selectAll")
    public String selectAll(int id,int current,Model model){
        User user = userService.getById(id);
        model.addAttribute("user", user);
        List<ClassInfo> classInfos = classInfoService.list();
        model.addAttribute("classes",classInfos);
        Page<BookInfo> page = new Page<>(current,5);
        bookInfoService.page(page,null);
        model.addAttribute("page",page);
        //判断是从哪条链接进入的查询，因为分页参数传递不同
        model.addAttribute("flag",0);
        if (user.getFlag()==0){
            return "html/admin/admin_books";
        } else {
            return "html/reader/reader_books";
        }
    }

    //分页条件查询
    @RequestMapping("/selectSome")
    public String selectSome(int id,int current,int classId,String name,Model model){
        User user = userService.getById(id);
        model.addAttribute("user", user);
        //保持搜索栏状态正常
        List<ClassInfo> classInfos = classInfoService.list();
        model.addAttribute("classes",classInfos);
        //用于条件查询并在搜索后保持搜索栏数据不变
        model.addAttribute("classId",classId);
        model.addAttribute("name",name);

        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        if (classId!=22&&classId!=0) {
            queryWrapper.eq("class_id",classId);
        }
        if (!name.isEmpty()) {
            queryWrapper.eq("name",name);
        }

        Page<BookInfo> page = new Page<>(current,5);
        bookInfoService.page(page,queryWrapper);

        model.addAttribute("page",page);
        //同上
        model.addAttribute("flag",1);

        if (user.getFlag()==0) {
            return "html/admin/admin_books";
        } else {
            return "html/reader/reader_books";
        }
    }

    //查看book详细信息
    @RequestMapping("/selectOne")
    public String selectOne(int id,int bookId,Model model){
        User user = userService.getById(id);
        model.addAttribute("user", user);
        BookInfo bookInfo = bookInfoService.getById(bookId);
        ClassInfo classInfo = classInfoService.getById(bookInfo.getClassId());
        model.addAttribute("book",bookInfo);
        model.addAttribute("class",classInfo);
        return "html/reader/reader_book_detail";
    }

    //跳转到添加图书页面
    @RequestMapping("/toAddBook")
    public String toAddBook(int id,Model model){
        User user = userService.getById(id);
        model.addAttribute("user", user);
        List<ClassInfo> classInfos = classInfoService.list();
        model.addAttribute("classes",classInfos);
        return "html/admin/admin_book_add";
    }

    //添加图书
    @RequestMapping("/addBook")
    public String addBook(int id,BookInfo bookInfo,Model model){
        model.addAttribute("id",id);
        bookInfoService.save(bookInfo);
        model.addAttribute("msg","提示:添加成功！");
        return "forward:/book/toAddBook";
    }

    //跳转到修改图书页面
    @RequestMapping("/toUpdateBook")
    public String toUpdateBook(int id,int current,int bookId, Model model){
        User user = userService.getById(id);
        model.addAttribute("user",user);

        model.addAttribute("current",current);

        List<ClassInfo> classes = classInfoService.list();
        model.addAttribute("classes",classes);

        BookInfo book = bookInfoService.getById(bookId);
        model.addAttribute("book",book);

        return "html/admin/admin_book_edit";
    }

    //修改图书
    @RequestMapping("/updateBook")
    public String updateBook(int id,int current,BookInfo bookInfo, Model model){
        model.addAttribute("id",id);
        model.addAttribute("current",current);

        bookInfoService.updateById(bookInfo);
        return "forward:/book/selectAll";
    }

    //删除图书
    @RequestMapping("/deleteBook")
    public String deleteBook(int id, int current,int bookId, Model model){
        User user = userService.getById(id);
        model.addAttribute("user",user);
        model.addAttribute("current",current);

        bookInfoService.removeById(bookId);
        return "forward:/book/selectAll";
    }



}
