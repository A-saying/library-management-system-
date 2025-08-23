package com.mc.libmanagement.reporsitory.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 * @TableName lend_list
 */
@TableName(value ="lend_list")
@Data
public class LendList implements Serializable {
    @TableId(value = "ser_num")
    private Integer serNum;
    private int bookId;
    private int readerId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lendDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date backDate;
    private int flag;
    private int lossFlag;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lossDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date payDate;
    /**
     * 在原来生成的代码的基础上，添加图书信息的类，作为LendList类的一部分
     * 方便查询数据库时，一起联表查询，并且封装数据 好在页面透出
     */
    private BookInfo bookInfo;
}