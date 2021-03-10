package com.tao.hai.concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockTest {
    /**读写锁分离的好处是在读多写少的场景中可大幅提升性能*/
    private static ReentrantReadWriteLock lock=new ReentrantReadWriteLock();
    private static ReentrantReadWriteLock.WriteLock writeLock=lock.writeLock();
    private static ReentrantReadWriteLock.ReadLock readLock=lock.readLock();
    private static Map<String,String> maps=new HashMap<>();
    //控制多个线程同时开始某动作的类：减计数方式
    private static CountDownLatch countDownLatch=new CountDownLatch(102);
    //可重复使用的栅栏：当所有线程都达到某个屏障点后再继续后续的操作：等待达到设定的量
    private static CyclicBarrier cyclicBarrier=new CyclicBarrier(102);

    public static void main(String[] args) throws InterruptedException{
        long beginTime=System.currentTimeMillis();
        for(int i=0;i<100;i++){
            new Thread(new ReadThread()).start();
        }
        for(int i=0;i<2;i++){
            new Thread(new WriteThread()).start();
        }
        countDownLatch.await();
        long endTime=System.currentTimeMillis();
        System.out.println("Consume time is: "+(endTime-beginTime)+"ms");
    }
    static class WriteThread implements Runnable{
        @Override
        public void run(){
            try{
                cyclicBarrier.await();
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                writeLock.lock();
                maps.put("1","2");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                writeLock.unlock();
            }
            countDownLatch.countDown();
        }
    }
    static class ReadThread implements Runnable{
        @Override
        public void run(){
            try{
                cyclicBarrier.await();
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                readLock.lock();
                maps.get("1");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                readLock.unlock();
            }
            countDownLatch.countDown();
        }
    }
}
