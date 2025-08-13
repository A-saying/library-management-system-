package com.mc.libmanagement.reporsitory.mapper;

import com.mc.libmanagement.reporsitory.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 19861
* @description 针对表【user】的数据库操作Mapper
* @createDate 2025-08-09 17:07:22
* @Entity reporsitory.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




