package com.tao.hai.dao.common;

import com.tao.hai.bean.common.Column;
import com.tao.hai.bean.common.Table;

import java.util.List;

public interface GeneratorDao {

    /**查询所有表*/
    public List<Table> queryTables();
    /**查询所有表*/
    public Table get(String tableName);
    /**查询表的所有字段*/
    public List<Column> listColumns(String tableName);
}
