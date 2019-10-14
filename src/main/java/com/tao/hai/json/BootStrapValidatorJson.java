package com.tao.hai.json;

import lombok.Data;

import java.io.Serializable;
/***采用bootStrap remote方式ajax验证时，必须返回valid信息，否则导致页面无法验证通过*/
@Data
public class BootStrapValidatorJson implements Serializable {
    /**是否有效*/
    private boolean valid;
    /**提示信息*/
    private String message;
    /**数据*/
    protected Object data;
    /**扩展数据*/
    protected Object extra;
}
