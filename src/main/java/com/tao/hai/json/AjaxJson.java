package com.tao.hai.json;

import lombok.Data;

import java.io.Serializable;

@Data
public class AjaxJson implements Serializable {
    /**
     * 是否成功
     */
    protected boolean success;
    /**
     * 是否有效,当为bootstrap验证方式时，必须读取valid属性
     */
    private boolean valid;
    /**
     * 状态码
     */
    protected int code;
    /**
     * 提示信息
     */
    protected String message;
    /**
     * 数据
     */
    protected Object data;
    /**
     * 扩展数据
     */
    protected Object extra;

    /**
     * 构造方法
     */
    public AjaxJson(boolean success) {
        this.success = success;
        this.valid = success;
    }
}
