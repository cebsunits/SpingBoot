package com.tao.hai.base;

import java.io.Serializable;

/**
 * 分页查询条件bean
 * @author sunits
 * @date 2019/7/18
 */
public class JsonModelBean implements Serializable {
    private String name;//表字段属性名
    private String value;//表字段属性值
    private String type;//类型名称：相当于某sql操作。llike:like '%n' ,rlike:like 'n%',like:like '%n%',eq:=,lt:<,le:<=,gt:>,ge:>=,in:in()

    public JsonModelBean(String name, String value, String type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

