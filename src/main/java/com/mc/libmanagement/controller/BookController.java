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
@RequestMapping("/book") // 类上加上路径， 用来在这个类的所有的请求路径上加 /book 的前缀
public class BookController {



    /**
     * 增： 增加一本图书   管理员  bookController中需要做的
     * 删： 删除一本图书   管理员 bookController中需要做的
     * 改：改变图书信息
     *     对图书信息的维护   管理员   bookController中需要做的
     *     借书：图书的数量 -1   读者  不在bookController中做，我们放到借阅记录控制器中
     *     还书：图书的数量 +1   读者   不在bookController中做，我们放到借阅记录控制器中
     * 查：查询图书列表   管理员、读者
     *      查询全部的图书（分页查询）   bookController中需要做的
     *      查询指定图书（根据名字查询、根据id查询）    bookController中需要做的
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
    // 分页的概念： 每一页显示的记录数，当前页码。将N条记录分到m个页面进行展示。

    /**
     * 查询全部的图书
     * @param id  人的id
     * @param current 当前查询的页码
     * @param model  SpringMVC框架提供的模型通道，用来给前后端提供数据透传
     * @return
     */
    @RequestMapping("/selectAll")
    public String selectAll(int id,int current,Model model){
        // 查询User表中id是xx的人
        User user = userService.getById(id);
        // 把人的信息放到SpringMVC框架提供模型中 => 把用户的信息透传给前端页面，可以在页面上显示用户的信息
        model.addAttribute("user", user);
        // 获取所有的class信息， list()方法是获取所有的数据
        List<ClassInfo> classInfos = classInfoService.list();
        // 同上，将class的信息放到模型中，透传给前端
        model.addAttribute("classes",classInfos);
        // 查询所有的图书数据，分页查询， 现在查询的是第current页
        // 新建了一个分页查询对象，page(当前页码，每页显示的记录数)
        //  Page<BookInfo> page  中的 BookInfo 表示查询的表, 在java中有个概念叫做泛型, 如果是分页查询用户信息，则写成 Page<User> page = new Page<>
        //  (current,5);
        Page<BookInfo> queryPage = new Page<>(current,5);
        // 通过page(queryPage,null)方法，将分页查询结果返回给queryPage对象， queryWrapper是查询条件，此处是null，则表示不需要查询条件==》 表中的数据全都可以
        // 带回数据到queryPage中后，可以通过getRecords()方法获取数据， getTotal 查询一共多少数据，getSize()查询一共有分页大小
        bookInfoService.page(queryPage,null);
        // 图书的分页数据查询都到了后，将数据放到模型中，透传给前端页面
        model.addAttribute("page",queryPage);
        //判断是从哪条链接进入的查询，因为分页参数传递不同, 此处的flag和user中的flag不一样，含义不同
        model.addAttribute("flag", 0);
        if (user.getFlag()==0){
            return "html/admin/admin_books";
        } else {
            return "html/reader/reader_books";
        }
    }

    /**
     * 根据条件，查询图书
     * @param id  人的id
     * @param current 当前查询的页码
     * @param classId 类别id
     * @param name 图书名称
     * @param model SpringMVC框架提供的模型通道，用来给前后端提供数据透传
     * @return
     */
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

        // 下面就是和 selectAll()方法 的区别

        // 新建了一个条件查询对象，QueryWrapper
        //  QueryWrapper<BookInfo> queryWrapper   中的 BookInfo 表示查询的表, 在java中有个概念叫做泛型
        //  如果是条件查询用户信息，则写成 QueryWrapper<User> queryWrapper
        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        // 下面要将条件添加进去  && 与  || 或 ！非
        // queryWrapper 中的常用方法：
        //   eq("class_id", classId), 表示表中的 class_id = classId   eq 是equal的缩写
        //   ne 不等于   gt 大于 。。。 详细可以进入到 QueryWrapper 类中查看
        if (classId != 0) {
            queryWrapper.eq("class_id",classId);
        }
        // name.isEmpty() 用来判断name这个值是不是空的， 如果name为空，则返回true， 否则返回false；
        // !name.isEmpty() 如果name为空，则返回false， 否则返回true；
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

    /**
     * 查看book详细信息
     * @param id  人的id
     * @param bookId 图书id
     * @param model SpringMVC框架提供的模型通道，用来给前后端提供数据透传
     * @return
     */
    @RequestMapping("/selectOne")
    public String selectOne(int id,int bookId,Model model){
        // 获取当前操作的user 并将其放到模型中
        User user = userService.getById(id);
        model.addAttribute("user", user);
        // 获取图书信息
        BookInfo bookInfo = bookInfoService.getById(bookId);
        // 获取图书类别信息
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
        // 保存图书
        bookInfoService.save(bookInfo);
        // 通过msg将信息带给前端
        model.addAttribute("msg","提示:添加成功！");
        // 跳转调用 /book/toAddBook 方法
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

        // 修改图书 根据bookInfo中的id，去查询到对应的记录，并将bookInfo中的数据更新到数据库的对应的记录中
        bookInfoService.updateById(bookInfo);
        return "forward:/book/selectAll";
    }

    //删除图书
    @RequestMapping("/deleteBook")
    public String deleteBook(int id, int current,int bookId, Model model){
        User user = userService.getById(id);
        model.addAttribute("user",user);
        model.addAttribute("current",current);

        // 删除图书 by id
        bookInfoService.removeById(bookId);
        return "forward:/book/selectAll";
    }



}
