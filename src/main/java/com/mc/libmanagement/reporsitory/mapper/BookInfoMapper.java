package com.mc.libmanagement.reporsitory.mapper;

import com.mc.libmanagement.reporsitory.domain.BookInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 19861
* @description 针对表【book_info(图书信息)】的数据库操作Mapper
* @createDate 2025-08-09 17:38:26
* @Entity com.mc.libmanagement.reporsitory.domain.BookInfo
*/
@Mapper
public interface BookInfoMapper extends BaseMapper<BookInfo> {

}




