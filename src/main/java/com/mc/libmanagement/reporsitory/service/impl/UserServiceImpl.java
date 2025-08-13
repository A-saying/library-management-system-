package com.mc.libmanagement.reporsitory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mc.libmanagement.reporsitory.domain.User;
import com.mc.libmanagement.reporsitory.mapper.UserMapper;
import com.mc.libmanagement.reporsitory.service.UserService;
import org.springframework.stereotype.Service;

/**
* @author 19861
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-08-09 17:07:22
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




