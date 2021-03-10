package com.tao.hai.proxy;
/**
 * 重量级对象，加载慢
 * */
public class DBQuery implements IDBQuery{
    public DBQuery(){
        try{
            //包含数据库连接等
            Thread.sleep(100);
        }catch (Exception e){

        }
    }
    @Override
    public String request(){
        return "request String:";
    }

}
