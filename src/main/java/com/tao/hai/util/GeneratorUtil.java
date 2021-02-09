package com.tao.hai.util;

import com.tao.hai.base.SystemException;
import com.tao.hai.bean.common.Column;
import com.tao.hai.bean.common.ColumnDO;
import com.tao.hai.bean.common.Table;
import com.tao.hai.bean.common.TableDO;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器工具类
 */
public class GeneratorUtil {


    /***获取配置文件*/
    public static PropertiesConfiguration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            e.printStackTrace();
            throw new SystemException("获取配置文件失败", e);
        }
    }

    public static List<String> getTemplates() {
        List<String> templates = new ArrayList<String>();
        templates.add("templates/common/generator/templates/domain.java.vm");
        templates.add("templates/common/generator/templates/Dao.java.vm");
        templates.add("templates/common/generator/templates/Dao.xml.vm");
        templates.add("templates/common/generator/templates/Service.java.vm");
        templates.add("templates/common/generator/templates/ServiceImpl.java.vm");
        templates.add("templates/common/generator/templates/Controller.java.vm");
        templates.add("templates/common/generator/templates/list.html.vm");
        templates.add("templates/common/generator/templates/add.html.vm");
        templates.add("templates/common/generator/templates/list.js.vm");
        templates.add("templates/common/generator/templates/add.js.vm");
        return templates;
    }

    /**
     * 生成代码方法
     */
    public static void generatorCode(Table table, List<Column> columns, ZipOutputStream zipOutputStream) {
        //配置信息
        PropertiesConfiguration configuration = getConfig();
        //表信息
        TableDO tableDO = new TableDO();
        tableDO.setTableName(table.getTableName());
        tableDO.setComments(table.getTableComment());
        //表名转换成Java类名
        String className = tableToJava(tableDO.getTableName(), configuration.getString("tablePrefix"), configuration.getBoolean("autoRemovePre"));
        tableDO.setClassName(className);
        tableDO.setHumpClassName(StringUtils.uncapitalize(className));
        //列信息
        List<ColumnDO> columnDOS = new ArrayList<ColumnDO>();
        List<ColumnDO> columnKeys = new ArrayList<ColumnDO>();
        for (Column column : columns) {
            ColumnDO columnDO = new ColumnDO();
            columnDO.setColumnName(column.getColumnName());
            columnDO.setDataType(column.getDataType());
            columnDO.setComments(column.getColumnComment());
            columnDO.setExtra(column.getExtra());
            //列名转换成Java属性名
            String attrName = columnToJava(columnDO.getColumnName());
            columnDO.setAttrName(attrName);
            columnDO.setHumpAttrName(StringUtils.uncapitalize(attrName));
            //列的数据类型，转换成Java类型
            String attrType = configuration.getString(columnDO.getDataType().toLowerCase(), "unknowType");
            columnDO.setAttrType(attrType);
            //是否主键
            String columnKey = column.getColumnKey();
            if ("PRI".equals(columnKey)) {
                columnKeys.add(columnDO);
            }
            columnDOS.add(columnDO);
        }
        //没主键，则第一个字段为主键
        if (ObjectUtils.isEmpty(columnKeys)) {
            columnKeys.add(tableDO.getColumns().get(0));
        }
        tableDO.setPk(columnKeys.get(0));
        tableDO.setColumns(columnDOS);
        //设置velocity资源加载器
        Properties properties = new Properties();
        properties.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(properties);
        //封装模板数据
        Map<String, Object> map = new HashMap<String, Object>(16);
        map.put("tableName", tableDO.getTableName());
        map.put("comments", tableDO.getComments());
        map.put("pk", tableDO.getPk());
        map.put("className", tableDO.getClassName());
        map.put("classname", tableDO.getHumpClassName());
        map.put("pathName", configuration.getString("package").substring(configuration.getString("package").lastIndexOf(".") + 1));
        map.put("columns", tableDO.getColumns());
        map.put("package", configuration.getString("package"));
        map.put("author", configuration.getString("author"));
        map.put("email", configuration.getString("email"));
        map.put("datetime", DateUtils.getDateTime());
        VelocityContext context = new VelocityContext(map);
        //获取模板列表
        List<String> templates = getTemplates();
        for(String template:templates){
            //渲染模板
            StringWriter writer=new StringWriter();
            Template tpl=Velocity.getTemplate(template,"UTF-8");
            tpl.merge(context,writer);
            String fileName=getFileName(template, tableDO.getHumpClassName(), tableDO.getClassName(), configuration.getString("package").substring(configuration.getString("package").lastIndexOf(".") + 1));
            try {
                ZipEntry zipEntry=new ZipEntry(fileName);
                zipOutputStream.putNextEntry(zipEntry);
                IOUtils.write(writer.toString(),zipOutputStream,"UTF-8");
                IOUtils.closeQuietly(writer);
                zipOutputStream.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
                throw new SystemException("渲染模板失败，表名：" + tableDO.getTableName(), e);
            }
        }
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String tablePrefix, boolean autoRemovePre) {
        if (autoRemovePre) {
            tableName = tableName.substring(tableName.indexOf("_") + 1);
        }
        if (StringUtils.isNotEmpty(tablePrefix)) {
            tableName = tableName.replace(tablePrefix, "");
        }
        return columnToJava(tableName);
    }

    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, String classname, String className, String packageName) {
        String packagePath = "main" + File.separator + "java" + File.separator;
        //String modulesname=config.getString("packageName");
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator;
        }

        if (template.contains("domain.java.vm")) {
            return packagePath + "domain" + File.separator + className + ".java";
        }

        if (template.contains("Dao.java.vm")) {
            return packagePath + "dao" + File.separator + className + "Dao.java";
        }

        if (template.contains("Service.java.vm")) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }

        if (template.contains("ServiceImpl.java.vm")) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if (template.contains("Controller.java.vm")) {
            return packagePath + "controller" + File.separator + className + "Controller.java";
        }

        if (template.contains("Dao.xml.vm")) {
            return "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + packageName + File.separator + className + "Dao.xml";
        }

        if (template.contains("list.html.vm")) {
            return "main" + File.separator + "resources" + File.separator + "templates" + File.separator
                    + packageName + File.separator + classname + File.separator + classname + ".html";
        }
        if (template.contains("add.html.vm")) {
            return "main" + File.separator + "resources" + File.separator + "templates" + File.separator
                    + packageName + File.separator + classname + File.separator + "add.html";
        }
        if (template.contains("edit.html.vm")) {
            return "main" + File.separator + "resources" + File.separator + "templates" + File.separator
                    + packageName + File.separator + classname + File.separator + "edit.html";
        }

        if (template.contains("list.js.vm")) {
            return "main" + File.separator + "resources" + File.separator + "static" + File.separator + "js" + File.separator
                    + "appjs" + File.separator + packageName + File.separator + classname + File.separator + classname + ".js";
        }
        if (template.contains("add.js.vm")) {
            return "main" + File.separator + "resources" + File.separator + "static" + File.separator + "js" + File.separator
                    + "appjs" + File.separator + packageName + File.separator + classname + File.separator + "add.js";
        }
        if (template.contains("edit.js.vm")) {
            return "main" + File.separator + "resources" + File.separator + "static" + File.separator + "js" + File.separator
                    + "appjs" + File.separator + packageName + File.separator + classname + File.separator + "edit.js";
        }
        return null;
    }
}
