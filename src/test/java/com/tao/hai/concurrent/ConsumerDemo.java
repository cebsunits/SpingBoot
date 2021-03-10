package com.tao.hai.concurrent;

import com.tao.hai.bean.PCData;

import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class ConsumerDemo implements Runnable {

    private BlockingQueue<PCData> queue;
    private static final int SLEEP_TIME=1000;
    public ConsumerDemo(BlockingQueue<PCData> queue){
        this.queue=queue;
    }

    @Override
    public void run() {
        System.out.println("start consumer name="+Thread.currentThread().getName());
        Random random=new Random();
        try {
            while (true){
                PCData pcData=queue.take();
                if(pcData!=null){
                    int re=pcData.getIntData()<<1;
                    System.out.println(MessageFormat.format("{0}*{1}={2}", pcData.getIntData(),pcData.getIntData(),re));
                    Thread.sleep(random.nextInt(SLEEP_TIME));
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
