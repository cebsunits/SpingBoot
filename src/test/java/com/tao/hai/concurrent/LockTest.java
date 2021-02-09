package com.tao.hai.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest  {

    volatile long vl=1L;

    ReentrantLock lock=new ReentrantLock();
    ConcurrentHashMap map=new ConcurrentHashMap();
    //psvm
    public static void main(String[] args) {
        //信号量，只能允许5个线程同时访问
        Semaphore semaphore=new Semaphore(5);
        try {
            //申请许可
            semaphore.acquire();
            //do somethings
            //释放许可
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
            semaphore.release();
        }

        ReadWriteLock readWriteLock=new ReadWriteLock() {
            @Override
            public Lock readLock() {
                return null;
            }

            @Override
            public Lock writeLock() {
                return null;
            }
        };
        int corePoolSize=5;
        int maximumPoolSize=10;
        long keepAliveTime=100;
        TimeUnit unit=TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue=new ArrayBlockingQueue<Runnable>(1000);
        ThreadPoolExecutor executor=new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,workQueue);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("threadName==="+Thread.currentThread().getName());
            }
        });
        executor.shutdown();
        while (true){
            if(executor.isTerminated()){
                System.out.println("All is completed!");
                break;
            }
        }
        //
        ExecutorService executorService= Executors.newCachedThreadPool();
        try {
            List<Future<String>> futureList=new ArrayList();
            int threadNum=10;
            for(int i=0;i<threadNum;i++){
                Future<String> future=executorService.submit(new TaskWithResult(i));
                futureList.add(future);
            }
            for(Future<String> f:futureList){
                System.out.println(f.get());
            }
            long curenttime=System.currentTimeMillis();
            //关闭线程池
            executorService.shutdown();
            while(true){
                if(executorService.isTerminated()){
                    System.out.println("All task is completed！");
                    System.out.println("time  spread:"+String.valueOf(System.currentTimeMillis()-curenttime));
                    break;
                }
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            executorService.shutdown();
        }
    }

}
