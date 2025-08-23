package com.mc.libmanagement.reporsitory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mc.libmanagement.reporsitory.domain.LendList;
import com.mc.libmanagement.reporsitory.service.LendListService;
import com.mc.libmanagement.reporsitory.mapper.LendListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
* @author 19861
* @description 针对表【lend_list】的数据库操作Service实现
* @createDate 2025-08-20 19:57:40
*/
@Service
public class LendListServiceImpl extends ServiceImpl<LendListMapper, LendList>
    implements LendListService{

    // mapper 映射，是将java中的对象和数据库中的表进行映射，通过mapper可以去操作数据库中的表
    @Resource
    private LendListMapper lendListMapper;


    @Override
    public List<LendList> selectByReader(int id) {
        return lendListMapper.selectByReader(id);
    }

    @Override
    public List<LendList> selectByReader2(int id) {
        return lendListMapper.selectByReader2(id);
    }

    @Override
    public void update1(int serNum, Date date) {
        lendListMapper.update1(serNum,date);
    }

    @Override
    public List<LendList> selectByReader3(int id) {
        return lendListMapper.selectByReader3(id);
    }

    @Override
    public void update2(int serNum) {
        lendListMapper.update2(serNum);
    }

    @Override
    public void update3(int serNum, Date date) {
        lendListMapper.update3(serNum,date);
    }

    @Override
    public void update4(int serNum, Date date) {
        lendListMapper.update4(serNum,date);
    }

    @Override
    public Date getDate(int serNum) {
        return lendListMapper.getDate(serNum);
    }

    @Override
    public void updateDate(int serNum, Date date1) {
        lendListMapper.updateDate(serNum,date1);
    }

    @Override
    public void updateFlag(int serNum) {
        lendListMapper.updateFlag(serNum);
    }

    @Override
    public void updateFlag2(int serNum) {
        lendListMapper.updateFlag2(serNum);
    }

    @Override
    public List<LendList> select1() {
        return lendListMapper.select1();
    }

    @Override
    public List<LendList> select2(int x) {
        return lendListMapper.select2(x);
    }

    @Override
    public List<LendList> select3(String y) {
        return lendListMapper.select3(y);
    }

    @Override
    public List<LendList> select4(int x, String y) {
        return lendListMapper.select4(x,y);
    }

    @Override
    public void updateOne(int serNum) {
        lendListMapper.updateOne(serNum);
    }
}




