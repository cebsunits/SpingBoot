package com.tao.hai.concurrent;
/**
 * 通过实现runnable接口方式创建线程，注意使用时需要通过Thread进行调用
 */

public class Mythread2 implements Runnable {

    public static void main(String[] args) {
        Mythread2 mythread2=new Mythread2();
        Thread thread=new Thread(mythread2);
        thread.start();
    }
    @Override
    public void run() {
        for(int i=0;i<1000;i++){
            System.out.println(i);
        }
    }
}
