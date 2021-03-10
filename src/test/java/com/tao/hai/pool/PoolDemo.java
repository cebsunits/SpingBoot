package com.tao.hai.pool;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.concurrent.atomic.AtomicInteger;

public class PoolDemo {
    static PooledObjectFactory factory=new PoolableObjectFactoryDemo();
    static ObjectPool pool=new GenericObjectPool(factory);
    private static AtomicInteger endCount=new AtomicInteger();
    public static class PoolTread extends Thread{
        public void run(){
            Object obj=null;
            try{
                for(int i=0;i<100;i++){
                    //从池中获取对象
                    obj=pool.borrowObject();
                    //模拟对象使用
                    System.out.println(obj+" is get ==="+i);
                    //使用完成后，将对象返回池中
                    pool.returnObject(obj);
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally {
                endCount.getAndIncrement();
            }
        }
    }
    /**main方法*/
    public static void main(String[] args) {
        new PoolTread().start();
        new PoolTread().start();
        new PoolTread().start();
        try{
            while (true){
                if(endCount.get()==3)
                    pool.close();
                    break;
            }
        }catch(Exception e){

        }
    }
}
