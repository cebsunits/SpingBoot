package com.tao.hai.concurrent;

import com.sun.corba.se.impl.orbutil.concurrent.Sync;

/**
 *acquire()和attempt(long mesc)方法的开始都要检查当前线程的中断标志是为了在当前线程已经被打断时可以立即返回，而不会在锁标志上等待。调用一个线程的interrupt()方法根据当前线程
 * 所处的状态，可能产生两种不同的结果，即当线程在运行过程中被打断，则设置当前线程的中断标志为true;当前线程阻塞于wait()\sleep()或join()方法 ，则当前线程的中断标志被清空，同时抛出
 * InterrupteException。release()方法简单的重置flg标志，并通知其他线程。attempt()方法是利用JAVA的Object.wait(long)进行计时的，由于Object.wait(long)不是一个精确的时钟，因此attempt()方法
 * 也是一个粗略的计时。
 *
 * */
public class MutexTest implements Sync {

    protected boolean flg=false;

    public void acquire() throws InterruptedException{
        if(Thread.interrupted()){
            throw new InterruptedException("中断异常");
        }
        synchronized (this){
            try{
                while (flg){
                    wait();
                }
                flg=true;
            }catch (InterruptedException e){
                notify();
                throw e;
            }

        }
    }

    public boolean attempt(long mesc) throws InterruptedException{
        if(Thread.interrupted())
            throw new InterruptedException("");
        synchronized (this){
            if(!flg){
                flg=true;
                return true;
            }else if(mesc<=0){
                return false;
            }else{
                long waitTime=mesc;
                long start=System.currentTimeMillis();
                try{
                    for(;;){
                        wait(waitTime);
                        if(!flg){
                            flg=true;
                            return true;
                        }else{
                            waitTime=mesc-(System.currentTimeMillis()-start);
                            if(waitTime<=0){
                                return false;
                            }
                        }
                    }
                }catch (InterruptedException e){
                    notify();
                    throw e;
                }
            }
        }
    }

    public void release(){
        flg=false;
        notify();
    }
}
