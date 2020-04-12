package com.tao.hai.json;

import org.springframework.http.HttpStatus;

public class AjaxError extends AjaxJson {
    /**
     * 构造方法
     */
    public AjaxError() {
        super(false);
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.message = "操作失败！";
    }

    /**
     * 构造方法
     */
    public AjaxError(String message) {
        super(false);
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.message = message;
    }
}
