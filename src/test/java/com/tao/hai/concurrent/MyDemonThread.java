package com.tao.hai.concurrent;

/**
 * 守护线程是一类特殊线程，和普通线程的区别在于其并不是应用程序的核心部分，当一个应用程序的所有非守护线程终止运行时，即使仍然有守护线程在运行，应用程序也将终止。
 * 反之，只要有一个非守护线程在运行，守护线程就不会终止。
 * */
public class MyDemonThread extends Thread {

    @Override
    public void run() {
        for(int i=0;i<1000;i++){
            try{
                Thread.sleep(1000);
            }catch (Exception e){

            }
            System.out.println("---current thread---"+i);
        }
    }

    public static void main(String[] args) {
        MyDemonThread myDemonThread=new MyDemonThread();
        myDemonThread.setDaemon(true);
        myDemonThread.start();
        for(int i=0;i<=10;i++){
            try{
                Thread.sleep(1000);
            }catch (Exception e){

            }
            System.out.println("---Damon thread--"+i);
        }
    }
}
