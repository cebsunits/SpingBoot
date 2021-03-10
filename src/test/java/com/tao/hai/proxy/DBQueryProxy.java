package com.tao.hai.proxy;

public class DBQueryProxy implements IDBQuery {
    private DBQuery real=null;
    @Override
    public String request(){
        //在真正需要的时候才创建真实对象，创建过程肯能比较慢
        if(real==null)
            real=new DBQuery();
        //多线程环境下，这里返回一个虚假类，类似于Future模式
        return real.request();
    }
}
