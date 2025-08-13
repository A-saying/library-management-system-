package com.mc.libmanagement.reporsitory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mc.libmanagement.reporsitory.domain.BookInfo;
import com.mc.libmanagement.reporsitory.service.BookInfoService;
import com.mc.libmanagement.reporsitory.mapper.BookInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author 19861
* @description 针对表【book_info(图书信息)】的数据库操作Service实现
* @createDate 2025-08-09 17:38:26
*/
@Service
public class BookInfoServiceImpl extends ServiceImpl<BookInfoMapper, BookInfo>
    implements BookInfoService{

}




