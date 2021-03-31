package com.tao.hai.design.observer;

/**
 * NASA 也能观察到有人登陆月球
 * */
public class Nasa implements LandingObserver {

    @Override
    public void observeLanding(String name) {
        if(name.contains("Apollo")){
            System.out.println("We made it!");
        }
    }
}
