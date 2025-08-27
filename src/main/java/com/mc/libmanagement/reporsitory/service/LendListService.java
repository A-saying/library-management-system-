package com.mc.libmanagement.reporsitory.service;

import com.mc.libmanagement.reporsitory.domain.LendList;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
* @author 19861
* @description 针对表【lend_list】的数据库操作Service
* @createDate 2025-08-20 19:57:40
*/

/**
 * interface 是接口， 定义双方沟通协议的 usb接口、type-c接口...
 * 接口定义了协议，只要协议一致，沟通的双方就不会有沟通的问题
 * 协议定义好后，后面怎么实现，那就是实现放的事情，怎么搞都行，只要符合协议
 * 在java中，接口中定义了什么？ 接口中定义的是方法，包含：方法的返回参数、方法名、入参参数
 *
 * class 是类，定义好interface之后，创建类，（implements）实现interface，通过@Override注解，实现interface中的方法，完成协议，完成实现
 */
public interface LendListService extends IService<LendList> {

    List<LendList> selectByReader(int id);

    /**
     * 根据用户id进行查询，返回已还、已赔偿的图书借阅信息
     * @param id
     * @return
     */
    List<LendList> selectByReader2(int id);

    /**
     * 更新借阅记录的丢失状态为1（丢失），将丢失时间更新为date
     * @param serNum
     * @param date
     */
    void update1(int serNum, Date date);

    /**
     * 根据用户id进行查询，返回未还并且挂失的图书借阅信息
     * @param id
     * @return
     */
    List<LendList> selectByReader3(int id);

    void update2(int serNum);

    void update3(int serNum, Date date);

    void update4(int serNum, Date date);

    Date getDate(int serNum);

    /**
     * 更新归还日期
     * @param serNum
     * @param date1
     */
    void updateDate(int serNum, Date date1);

    void updateFlag(int serNum);

    void updateFlag2(int serNum);

    List<LendList> select1();

    List<LendList> select2(int x);

    List<LendList> select3(String y);

    List<LendList> select4(int x, String y);


    void updateOne(int serNum);

}
