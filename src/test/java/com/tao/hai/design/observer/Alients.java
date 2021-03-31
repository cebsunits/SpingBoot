package com.tao.hai.design.observer;
/**
 * 外星人观察到人类登陆月球
 * */
public class Alients implements LandingObserver {
    @Override
    public void observeLanding(String name) {
        if(name.contains("Apollo")){
            System.out.println("They are distracted,lets invade earth!");
        }
    }
}
