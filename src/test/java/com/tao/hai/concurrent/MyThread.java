package com.tao.hai.concurrent;

/**
 * 通过Thread方式创建线程
 * */
public class MyThread extends Thread {

    public static void main(String[] args) {
        MyThread myThread=new MyThread();
        myThread.start();
    }

    @Override
    public void run() {

        for(int i=0;i<1000;i++){
            System.out.println(i);
        }
    }
}
