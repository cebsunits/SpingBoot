package com.tao.hai.concurrent;

import com.tao.hai.bean.PCData;

import java.util.concurrent.*;

public class ProducerConsumerTest {
    public static void main(String[] args) throws Exception {
        BlockingQueue<PCData> queue=new LinkedBlockingQueue<>() ;
        ProducerDemo p1=new ProducerDemo(queue);
        ProducerDemo p2=new ProducerDemo(queue);
        ProducerDemo p3=new ProducerDemo(queue);
        ProducerDemo p4=new ProducerDemo(queue);
        ConsumerDemo c1=new ConsumerDemo(queue);
        ConsumerDemo c2=new ConsumerDemo(queue);
        ConsumerDemo c3=new ConsumerDemo(queue);
        ConsumerDemo c4=new ConsumerDemo(queue);
        ExecutorService service= Executors.newCachedThreadPool();
        service.execute(p1);
        service.execute(p2);
        service.execute(p3);
        service.execute(p4);
        service.execute(c1);
        service.execute(c2);
        service.execute(c3);
        service.execute(c4);
        Thread.sleep(1000);
        p1.stop();
        p2.stop();
        p3.stop();
        p4.stop();
        Thread.sleep(3000);
        service.shutdown();

        int corePoolSize=6;
        int maximumPoolSize=12;
        long keepAliveTime=1000;
        TimeUnit unit=TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue=new PriorityBlockingQueue<>();
        ExecutorService service1=new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,workQueue);



    }
}
