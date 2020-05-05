package com.tao.hai.service;


import com.tao.hai.bean.common.Column;
import com.tao.hai.bean.common.Table;

import java.util.List;

/**
 * @description 生成代码接口
 * */
public interface GeneratorService {

    /**查询所有表*/
    public List<Table> queryTables();
    /**查询所有表*/
    public List<Column> listColumns(String tableName);
    /**根据表生成代码接口*/
    public byte[] generatorCode(String[] tables);

}
