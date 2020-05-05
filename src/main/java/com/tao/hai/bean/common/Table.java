package com.tao.hai.bean.common;

import lombok.Data;

import java.util.Date;
@Data
public class Table {

    //表的名称
    private String tableName;
    //表的备注
    private String tableComment;
    //創建时间
    private Date createTime;

}
