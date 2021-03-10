package com.tao.hai.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
/***
 *
 * JDK动态代理
 * */
public class JdkDbQueryHandler implements InvocationHandler {
    IDBQuery real=null;
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
        if(real==null)
            real=new DBQuery();//如果是第一次调用，则生成真实对象
        return real.request();//使用真实主题完成实际的操作
    }
}
