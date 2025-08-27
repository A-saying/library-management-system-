package com.mc.libmanagement.reporsitory.mapper;

import com.mc.libmanagement.reporsitory.domain.LendList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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

    void update1(int serNum, Date date);

    List<LendList> selectByReader3(int id);

    void update2(int serNum);

    void update3(int serNum, Date date);

    void update4(int serNum, Date date);

    Date getDate(int serNum);

    void updateDate(int serNum, Date date1);

    void updateFlag(int serNum);

    void updateFlag2(int serNum);

    List<LendList> select1();

    List<LendList> select2(int x);

    List<LendList> select3(String y);

    List<LendList> select4(int x, String y);

    void updateOne(int serNum);
}




