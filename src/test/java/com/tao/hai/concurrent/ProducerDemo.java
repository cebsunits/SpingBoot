package com.tao.hai.concurrent;

import com.tao.hai.bean.PCData;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerDemo implements Runnable {
    private volatile boolean isRunning=true;
    private BlockingQueue<PCData> queue;
    private static AtomicInteger count=new AtomicInteger(0);
    private static final int SLEEP_TIME=1000;
    public ProducerDemo(BlockingQueue<PCData> queue){
        this.queue=queue;
    }

    public void run(){
        PCData pcData=null;
        Random random=new Random();
        System.out.println("start producer id="+Thread.currentThread().getName());
        try {
            while (isRunning){
                Thread.sleep(random.nextInt(SLEEP_TIME));
                pcData=new PCData(count.getAndIncrement());
                System.out.println(pcData+" is put to queue");
                //提交数据到缓冲区
                if(!queue.offer(pcData,2, TimeUnit.SECONDS)){
                    System.out.println("failed to put data:"+pcData);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        isRunning=false;
    }
}
