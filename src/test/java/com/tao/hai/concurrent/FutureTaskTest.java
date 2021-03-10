package com.tao.hai.concurrent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
/**
 * FutureTask 模式核心在于去除了主函数中的等待时间，并使得原本需要等待时间段可以用于处理其他业务逻辑，从而充分利用系统资源
 *
 * */

public class FutureTaskTest {

    private Map<String, FutureTask> connectionMap=new ConcurrentHashMap<String,FutureTask>();
    /**获取连接池对象，不加锁情况*/
    public Connection getConnection(String key)throws InterruptedException, ExecutionException {
        FutureTask<Connection> connectionFutureTask=connectionMap.get(key);
        if(connectionFutureTask!=null){
            return connectionFutureTask.get();
        }else{
            Callable<Connection> callable=new Callable<Connection>() {
                @Override
                public Connection call() throws Exception {
                    //等待时间长的业务逻辑
                    return getConnectionLock("getConnection");
                }
            };
            FutureTask<Connection> newTask=new FutureTask<>(callable);
            connectionFutureTask=connectionMap.putIfAbsent(key,newTask);
            if(connectionFutureTask==null){
                connectionFutureTask=newTask;
                connectionFutureTask.run();
            }
            //获取call()方法的返回值，如果call方法未执行完成，则会等待
            return connectionFutureTask.get();
        }
    }
    /**加锁情况*/
    private Map<String, Connection> connectionPool=new ConcurrentHashMap<String,Connection>();
    ReentrantLock lock=new ReentrantLock();
    public Connection getConnectionLock(String key) throws SQLException {
        lock.lock();
        if(connectionPool.containsKey(key)){
            return connectionPool.get(key);
        }else{
            String url="";
            Connection connection=DriverManager.getConnection(url);
            connectionPool.put(key,connection);
            return connection;
        }
    }

    /**控制连接池个数*/
    /**1、传统方式*/
    int maxActive;
    int numActive;
    int maxWait;
    LinkedList pool;
    public void get() throws InterruptedException{
        long startTime=System.currentTimeMillis();
        Object object=null;
        for(;;){
            synchronized(this){
                object=pool.removeFirst();
                if(object==null&&numActive>=maxActive){
                    long waitTime=maxWait-(System.currentTimeMillis()-startTime);
                    wait(waitTime);
                    if(System.currentTimeMillis()-startTime>maxWait){
                        //超时
                    }else {
                        continue;
                    }
                }
                numActive++;
            }
            //if needed then create object & validate object
            //return object;
        }
    }
    /**2、通过Semaphore,减少了同步代码块内容*/
    Semaphore semaphore;
    public void get1() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        Object object = null;
        Object pair;
        for (; ; ) {
            final long elapsed=System.currentTimeMillis()-startTime;
            final long waitTime=maxWait-elapsed;
            boolean timeouted=semaphore.tryAcquire(waitTime,TimeUnit.MILLISECONDS);
            if(!timeouted){
                throw new NoSuchElementException("timeout waiting for idle object");
            }else{
                try {
                    synchronized (pool){
                        pair=pool.removeFirst();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) {

    }

}
