package com.tao.hai.base;

import lombok.Data;

/**
 * 系统异常定义
 */
@Data
public class SystemException extends RuntimeException {
    /***消息*/
    private String message;
    /**
     * 异常代码
     */
    private int code = 500;

    public SystemException(String message) {
        super(message);
        this.message = message;
    }

    public SystemException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public SystemException(String message, Throwable e, int code) {
        super(message, e);
        this.message = message;
        this.code = code;
    }
}
