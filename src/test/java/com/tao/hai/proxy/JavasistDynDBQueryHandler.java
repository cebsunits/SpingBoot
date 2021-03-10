package com.tao.hai.proxy;

import javassist.util.proxy.MethodHandler;

import java.lang.reflect.Method;
/**
 * javasist 动态代理
 * */
public class JavasistDynDBQueryHandler implements MethodHandler {
    IDBQuery real=null;
    @Override
    public Object invoke(Object var1, Method var2, Method var3, Object[] var4) throws Throwable{
        if(real==null)
            real=new DBQuery();
        return real.request();
    }
}
