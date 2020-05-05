package com.tao.hai.service.impl;

import com.tao.hai.bean.common.Column;
import com.tao.hai.bean.common.Table;
import com.tao.hai.dao.common.GeneratorDao;
import com.tao.hai.service.GeneratorService;
import com.tao.hai.util.GeneratorUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.ZipOutputStream;

@Service
public class GeneratorServiceImpl implements GeneratorService {

    @Autowired
    GeneratorDao generatorDao;
    @Override
    public List<Table> queryTables() {
        return generatorDao.queryTables();
    }
    @Override
    public List<Column> listColumns(String tableName) {
        return generatorDao.listColumns(tableName);
    }
    @Override
    public byte[] generatorCode(String[] tables) {
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream=new ZipOutputStream(outputStream);
        if(tables!=null&&tables.length>0){
            for(String tableName:tables){
                /**查询表信息*/
                Table table=generatorDao.get(tableName);
                /**查詢列信息*/
                List<Column> columns=generatorDao.listColumns(tableName);
                /**生成代码*/
                GeneratorUtil.generatorCode(table,columns,zipOutputStream);
            }
        }
        IOUtils.closeQuietly(zipOutputStream);
        return outputStream.toByteArray();
    }
}
