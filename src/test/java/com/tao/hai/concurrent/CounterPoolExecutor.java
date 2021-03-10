package com.tao.hai.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CounterPoolExecutor extends ThreadPoolExecutor {
    private AtomicInteger count=new AtomicInteger(0);
    public long startTime=0;
    public String funcName;
    public static int TASK_COUNT=10000;
    public CounterPoolExecutor(int corePoolSize,
                               int maximumPoolSize,
                               long keepAliveTime,
                               TimeUnit unit,
                               BlockingQueue<Runnable> workQueue){
        super(corePoolSize,maximumPoolSize,keepAliveTime,unit,workQueue);
    }

    protected void beforeExecute(Thread t, Runnable r){
        System.out.println("beforeExecute mythread name "+t.getName()+" threadId "+t.getId());
    }
    protected void afterExecute(Runnable r, Throwable t){
        System.out.println("afterExecute mythread name "+Thread.currentThread().getName());
        //没执行完一个任务加1
        count.getAndIncrement();
        int total=count.get();
        if(total==TASK_COUNT)
            System.out.println(funcName+" spend time "+(System.currentTimeMillis()-startTime));
    }
}
