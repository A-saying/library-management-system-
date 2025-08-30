package com.mc.libmanagement.reporsitory.mapper;

import com.mc.libmanagement.reporsitory.domain.LendList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* @author 19861
* @description 针对表【lend_list】的数据库操作Mapper
* @createDate 2025-08-20 19:57:40
* @Entity com.mc.libmanagement.reporsitory.domain.LendList
*/
@Mapper
public interface LendListMapper extends BaseMapper<LendList> {

    List<LendList> selectByReader(int id);

    /**
     * 查询指定读者id所有已还/已赔偿的图书借阅记录
     * @param id
     * @return
     */
    List<LendList> selectByReader2(int id);

    void update1(@Param("serNum")int serNum, @Param("date")Date date);

    List<LendList> selectByReader3(int id);

    /**
     * 更新借阅记录为解挂 loss_flag = 0, loss_date = null
     * @param serNum
     */
    void update2(int serNum);

    void update3(@Param("serNum")int serNum, @Param("date")Date date);

    /**
     * @Param("serNum") 和 @Param("date") 是为了告诉mybatis，参数的名称是什么，即在sql语句中怎么识别参数
     * 如果方法中有多个参数，则需要加这个注解
     * 如果方法中只有一个参数，则不需要加
     * @param serNum
     * @param date
     */
    void update4(@Param("serNum")int serNum, @Param("date")Date date);

    Date getDate(int serNum);

    void updateDate(@Param("serNum")int serNum, @Param("date1")Date date1);

    void updateFlag(int serNum);

    void updateFlag2(int serNum);

    List<LendList> select1();

    List<LendList> select2(int x);

    List<LendList> select3(String y);

    List<LendList> select4(@Param("x")int x, @Param("y")String y);

    void updateOne(int serNum);
}




