package com.tao.hai.design.templet;
/**
 * 如果申请失败，函数接口 Criteria 抛出异常
 * */
public interface Criteria {
    public void check() throws ApplicationDenied;
}
