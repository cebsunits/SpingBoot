package com.tao.hai.proxy;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibDBQueryInterceptor implements MethodInterceptor {
    IDBQuery real=null;

    @Override
    public Object intercept(Object var1, Method var2, Object[] var3, MethodProxy var4) throws Throwable{
        if(real==null)
            real=new DBQuery();
        return real.request();
    }
}
