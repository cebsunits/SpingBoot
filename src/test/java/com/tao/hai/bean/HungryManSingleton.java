package com.tao.hai.bean;
/**
 * 饿汉式创建单例对象
 * */
public class HungryManSingleton {
    private HungryManSingleton(){

    }
    private static final HungryManSingleton hungryMan=new HungryManSingleton();
    // 问题:  如果此类内部 有很多的变量 会造成性能问题
    public static HungryManSingleton getInstance(){
        return hungryMan;
    }
}
