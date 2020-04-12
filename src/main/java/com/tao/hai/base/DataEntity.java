package com.tao.hai.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class DataEntity<T> implements Serializable {
    /**
     * 是否新纪录，默认为true
     */
    private boolean isNewRecord = true;

}
