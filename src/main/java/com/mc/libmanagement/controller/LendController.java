package com.mc.libmanagement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mc.libmanagement.reporsitory.domain.BookInfo;
import com.mc.libmanagement.reporsitory.domain.LendList;
import com.mc.libmanagement.reporsitory.domain.User;
import com.mc.libmanagement.reporsitory.service.BookInfoService;
import com.mc.libmanagement.reporsitory.service.LendListService;
import com.mc.libmanagement.reporsitory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/lend")
public class LendController {

    /**
     * 读者登录
     *      查询所有未还图书
     *      查询所有已还图书
     *      删除借还信息（只能删除已经归还的图书）
     *      挂失
     *      解挂
     *      赔偿
     *      归还
     * 管理员登录：
     *      催还
     *      查询所有的 借还信息
     *      查询所有未还图书
     *      删除操作（都可以删）
     *
     *  分析需要哪些服务参与：
     *      LendListService  借阅记录的服务，用于处理借、还、催还、赔偿等
     *      UserService  用户的服务，用于处理查询等
     *      BookInfoService  图书的服务，用于图书的信息查询、库存变更、赔偿查询
     */

    /**
     * 引入所需资源
     */
    @Resource
    private LendListService lendListService;
    @Resource
    private BookInfoService bookInfoService;
    @Resource
    private UserService userService;

    //查询当前用户的借阅记录（读者登录）
    @RequestMapping("/selectSome")
    public String selectSome(int id, int current, Model model) {
        // 获取当前用户
        User user = userService.getById(id);
        model.addAttribute("user", user);

        // 根据用户去查询借阅信息
        // MybatisX 生成的代码LendListService中是没有 selectByReader()方法
        // 因为通过MybatisX生成的代码中，只会关联一个表 --> lend_list
        // 要查询的信息包含：图书的借阅记录和借阅图书的详细信息
        // 所以：selectByReader方法，会涉及两张表： lend_list 、book_info
        // 这个方法 获取了 借阅人是id的所有的借阅记录 + 借阅图书的数据
        List<LendList> lendList = lendListService.selectByReader(id);
        // lendList 中 借阅记录的id分别是： 0，1，2，3，4，5，6，7，8，9，10
        Page<LendList> page = new Page<>(current, 5);
        //总数据数
        int count = lendList.size();
        //总页数
        //  6 % 5 == 1, 7 / 5 == 1
        int pages = count % 5 == 0 ? count / 5 : count / 5 + 1;
        //让页码保持在合理范围
        if (current <= 0) {
            current = 1;
        }
        if (current > pages) {
            current = pages;
        }
        //计算当前页第一条数据的下标
        // 一共有11条借阅记录
        // 记录分组后的下标 应该如下  0，1，2，3，4，5，6，7，8，9，10
        // 0,1,2,3,4
        // 5,6,7,8,9
        // 10
        // currId 是 currentId的缩写，是当前页的记录的开始的id
        int currId = (current - 1) * 5;

        // 获取当前日期
        Date date1 = new Date();
        // 新建一个List,作为当前页返回结果的集合
        List<LendList> pageList = new ArrayList<>();
        // 循环 i < count -currId 是为了保证，数据不越界，即，拿取的记录不会超过我的最大记录值
        for (int i = 0; i < 5 && i < count - currId; i++) {
            // 获取图书记录的 归还日期
            Date date2 = lendList.get(currId + i).getBackDate();
            // date1.compareTo(date2) 比较 date1 和 date2 的大小，
            // 如果 date1 大于 date2，则返回正数，如果 date1 小于 date2，则返回负数，如果 date1 等于 date2，则返回0
            // 如果当前日期 > 应该归还日期 即 已经超出归还日期
            if (date1.compareTo(date2) == 1) {
                // 当前的状态还是 未还状态
                if (lendList.get(currId + i).getFlag() == 0) {
                    // 将归还的flag 设置为 2 （超期）
                    lendListService.updateFlag(lendList.get(currId + i).getSerNum());
                }
            }
            // 如果当前日期 <= 归还日期
            if (date1.compareTo(date2) < 1) {
                // 如果当前状态是 2（超期）
                if (lendList.get(currId + i).getFlag() == 2) {
                    // 将状态设置为 0 (正常)
                    lendListService.updateFlag2(lendList.get(currId + i).getSerNum());
                }
            }

            // 将数据逐条添加到pageList中，用于返回数据给到页面
            pageList.add(lendList.get(currId + i));
        }
        // 组装返回页面的对象Page
        // 页面的大小
        page.setSize(5);
        // 当前的页码
        page.setCurrent(current);
        // 总条数
        page.setTotal(count);
        //计算分页总页数
        page.setPages(pages);
        // 当前页的记录
        page.setRecords(pageList);

        model.addAttribute("page", page);
        return "html/reader/reader_return_books";
    }

    //查询所有已还书
    @RequestMapping("/selectAll")
    public String selectAll(int id, int current, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);

        List<LendList> lendList = lendListService.selectByReader2(id);

        Page<LendList> page = new Page<>(current, 5);
        //总数据数
        int count = lendList.size();
        //总页数
        int pages = count % 5 == 0 ? count / 5 : count / 5 + 1;
        //让页码保持在合理范围
        if (current == 0) {
            current++;
        }
        if (current > pages) {
            current--;
        }
        //计算当前页第一条数据的下标
        int currId = current > 1 ? (current - 1) * 5 : 0;

        List<LendList> pageList = new ArrayList<>();
        for (int i = 0; i < 5 && i < count - currId; i++) {
            pageList.add(lendList.get(currId + i));
        }
        page.setSize(5);
        page.setCurrent(current);
        page.setTotal(count);
        //计算分页总页数
        page.setPages(pages);
        page.setRecords(pageList);

        model.addAttribute("page", page);
        return "html/reader/reader_lend_list";
    }

    //删除借还信息
    @RequestMapping("/deleteOne")
    public String deleteOne(int serNum, int id, int current, Model model) {
        lendListService.removeById(serNum);
        User user = userService.getById(id);
        model.addAttribute("id", user.getId());
        model.addAttribute("current", current);
        return "forward:/lend/selectAll";
    }

    //图书借阅
    @RequestMapping("/addOne")
    public String addOne(int id, int bookId, int current, int classId, int flag, String name, Model model) {
        //获取当前系统时间
        Date date1 = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        //修改为30天后
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date date2 = calendar.getTime();

        //添加一条借阅信息
        LendList lendList = new LendList();
        lendList.setBookId(bookId);
        lendList.setReaderId(id);
        lendList.setLendDate(date1);
        lendList.setBackDate(date2);
        lendList.setFlag(0);
        lendList.setLossFlag(0);
        lendListService.save(lendList);

        //更新图书存量
        BookInfo book = bookInfoService.getById(bookId);
        int number = book.getNumber() - 1;
        book.setNumber(number);
        bookInfoService.updateById(book);


        if (flag == 0) {
            System.out.println(classId);
            System.out.println(name);
            model.addAttribute("id", id);
            model.addAttribute("current", current);
            return "forward:/book/selectAll";
        } else {
            model.addAttribute("id", id);
            model.addAttribute("current", current);
            model.addAttribute("classId", classId);
            model.addAttribute("name", name);
            return "forward:/book/selectSome";
        }
    }

    //图书挂失页面
    @RequestMapping("/toUpdateOne")
    public String toUpdateOne(int id, int current, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);

        List<LendList> lendList = lendListService.selectByReader(id);

        Page<LendList> page = new Page<>(current, 5);
        //总数据数
        int count = lendList.size();
        //总页数
        int pages = count % 5 == 0 ? count / 5 : count / 5 + 1;
        //让页码保持在合理范围
        if (current == 0) {
            current++;
        }
        if (current > pages) {
            current--;
        }
        //计算当前页第一条数据的下标
        int currId = current > 1 ? (current - 1) * 5 : 0;

        List<LendList> pageList = new ArrayList<>();

        Date date1 = new Date();
        for (int i = 0; i < 5 && i < count - currId; i++) {
            Date date2 = lendList.get(currId + i).getBackDate();
            if (date1.compareTo(date2) == 1) {
                if (lendList.get(currId + i).getFlag() == 0) {
                    lendListService.updateFlag(lendList.get(currId + i).getSerNum());
                }
            }
            if (date1.compareTo(date2) < 1) {
                if (lendList.get(currId + i).getFlag() == 2) {
                    lendListService.updateFlag2(lendList.get(currId + i).getSerNum());
                }
            }

            pageList.add(lendList.get(currId + i));
        }
        page.setSize(5);
        page.setCurrent(current);
        page.setTotal(count);
        //计算分页总页数
        page.setPages(pages);
        page.setRecords(pageList);

        model.addAttribute("page", page);
        return "html/reader/reader_guashi_books";
    }

    //图书挂失操作
    @RequestMapping("/updateOne")
    public String updateOne(int id, int current, int serNum, Model model) {
        model.addAttribute("current", current);
        model.addAttribute("id", id);
        Date date = new Date();
        lendListService.update1(serNum, date);
        return "forward:/lend/toUpdateOne";
    }

    //图书解挂页面
    @RequestMapping("/toUpdateTwo")
    public String toUpdateTwo(int id, int current, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);

        List<LendList> lendList = lendListService.selectByReader3(id);

        Page<LendList> page = new Page<>(current, 5);
        //总数据数
        int count = lendList.size();
        //总页数
        int pages = count % 5 == 0 ? count / 5 : count / 5 + 1;
        //让页码保持在合理范围
        if (current == 0) {
            current++;
        }
        if (current > pages) {
            current--;
        }
        //计算当前页第一条数据的下标
        int currId = current > 1 ? (current - 1) * 5 : 0;

        List<LendList> pageList = new ArrayList<>();
        for (int i = 0; i < 5 && i < count - currId; i++) {
            pageList.add(lendList.get(currId + i));
        }
        page.setSize(5);
        page.setCurrent(current);
        page.setTotal(count);
        //计算分页总页数
        page.setPages(pages);
        page.setRecords(pageList);

        model.addAttribute("page", page);
        return "html/reader/reader_jiegua_books";
    }

    //图书解挂操作
    @RequestMapping("/updateTwo")
    public String updateTwo(int id, int current, int serNum, Model model) {
        model.addAttribute("current", current);
        model.addAttribute("id", id);

        lendListService.update2(serNum);
        return "forward:/lend/toUpdateTwo";
    }

    //图书赔偿操作
    @RequestMapping("/updateThree")
    public String updateThree(int id, int current, int serNum, int bookId, Model model) {
        model.addAttribute("current", current);
        model.addAttribute("id", id);
        Date date = new Date();
        lendListService.update3(serNum, date);
        //更新图书存量
        BookInfo book = bookInfoService.getById(bookId);
        int number = book.getNumber() + 1;
        book.setNumber(number);
        bookInfoService.updateById(book);

        return "forward:/lend/toUpdateTwo";
    }

    //图书归还操作
    @RequestMapping("/updateFour")
    public String updateFour(int id, int current, int serNum, int bookId, Model model) {
        model.addAttribute("current", current);
        model.addAttribute("id", id);
        Date date = new Date();
        lendListService.update4(serNum, date);
        //更新图书存量
        BookInfo book = bookInfoService.getById(bookId);
        int number = book.getNumber() + 1;
        book.setNumber(number);
        bookInfoService.updateById(book);

        return "forward:/lend/selectSome";
    }

    //图书续借操作
    @RequestMapping("/updateFive")
    public String updateFive(int id, int current, int serNum, Model model) {
        model.addAttribute("current", current);
        model.addAttribute("id", id);
        //获取当前的归还时间
        Date date = lendListService.getDate(serNum);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //修改为30天后
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date date1 = calendar.getTime();

        lendListService.updateDate(serNum, date1);

        return "forward:/lend/selectSome";
    }

    //管理员的查询全部借还信息
    @RequestMapping("/select1")
    public String select1(int id, int current, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);

        List<LendList> lendList = lendListService.select1();

        Page<LendList> page = new Page<>(current, 5);
        //总数据数
        int count = lendList.size();
        //总页数
        int pages = count % 5 == 0 ? count / 5 : count / 5 + 1;
        //让页码保持在合理范围
        if (current == 0) {
            current++;
        }
        if (current > pages) {
            current--;
        }
        //计算当前页第一条数据的下标
        int currId = current > 1 ? (current - 1) * 5 : 0;

        List<LendList> pageList = new ArrayList<>();
        for (int i = 0; i < 5 && i < count - currId; i++) {
            pageList.add(lendList.get(currId + i));
        }
        page.setSize(5);
        page.setCurrent(current);
        page.setTotal(count);
        //计算分页总页数
        page.setPages(pages);
        page.setRecords(pageList);

        model.addAttribute("page", page);
        model.addAttribute("z", 0);
        return "html/admin/admin_lend_list";
    }

    //管理员的查询全部借还信息
    @RequestMapping("/select2")
    public String select2(int id, int current, int x, String y, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        model.addAttribute("x", x);
        model.addAttribute("y", y);

        List<LendList> lendList = new ArrayList<>();

        if (x != 4 && y.isEmpty()) {
            lendList = lendListService.select2(x);
        } else if (x == 4 && !y.isEmpty()) {
            lendList = lendListService.select3(y);
        } else if (x != 4 && !y.isEmpty()) {
            lendList = lendListService.select4(x, y);
        }

        Page<LendList> page = new Page<>(current, 5);
        //总数据数
        int count = lendList.size();
        //总页数
        int pages = count % 5 == 0 ? count / 5 : count / 5 + 1;
        //让页码保持在合理范围
        if (current == 0) {
            current++;
        }
        if (current > pages) {
            current--;
        }
        //计算当前页第一条数据的下标
        int currId = current > 1 ? (current - 1) * 5 : 0;

        List<LendList> pageList = new ArrayList<>();
        for (int i = 0; i < 5 && i < count - currId; i++) {
            pageList.add(lendList.get(currId + i));
        }
        page.setSize(5);
        page.setCurrent(current);
        page.setTotal(count);
        //计算分页总页数
        page.setPages(pages);
        page.setRecords(pageList);

        model.addAttribute("page", page);
        model.addAttribute("z", 1);

        return "html/admin/admin_lend_list";
    }

    //催还操作
    @RequestMapping("/update1")
    public String update1(int id, int current, int serNum, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("current", current);

        lendListService.updateOne(serNum);

        return "forward:/lend/select1";
    }

    //删除操作
    @RequestMapping("/delete")
    public String delete(int id, int current, int serNum, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("current", current);

        lendListService.removeById(serNum);

        return "forward:/lend/select1";
    }
}
