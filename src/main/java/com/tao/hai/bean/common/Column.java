package com.tao.hai.bean.common;

import lombok.Data;

@Data
public class Column {

    // 列名
    private String columnName;
    // 列名类型
    private String dataType;
    // 列名备注
    private String columnComment;
    //主键标识
    private String columnKey;
    //auto_increment
    private String extra;
}
