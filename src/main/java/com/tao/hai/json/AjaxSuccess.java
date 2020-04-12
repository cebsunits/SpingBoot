package com.tao.hai.json;

import org.springframework.http.HttpStatus;

public class AjaxSuccess extends AjaxJson {
    /**
     * 构造方法
     */
    public AjaxSuccess() {
        super(true);
        this.code = HttpStatus.OK.value();
        message = "操作成功！";
    }

    /**
     * 构造方法
     */
    public AjaxSuccess(String message) {
        super(true);
        this.code = HttpStatus.OK.value();
        this.message = message;
    }
}
