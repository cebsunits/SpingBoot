package com.tao.hai.bean;
/**
 * 静态内部类创建单实例对象，延迟加载，线程安全
 * */
public class StaticInnerSingleton {
    private StaticInnerSingleton(){

    }
    private static StaticInnerSingleton getInstance(){
        return InnerClass.instance;
    }

    public static class InnerClass{
        private static final StaticInnerSingleton instance=new StaticInnerSingleton();
    }
}
