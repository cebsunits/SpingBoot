package com.tao.hai.design.observer;

public class ObserverDemo {
    public static void main(String[] args) {
        Moon moon=new Moon();
        moon.startSpying(new Nasa());
        moon.startSpying(new Alients());
        moon.land("Apollo land");
        moon.land("landing moon");
        //use lambda,can not create Nasa and Alients object
        moon.startSpying(name -> {if(name.contains("Apollo")){
            System.out.println("We made it!");
        }});
        moon.startSpying(name -> {if(name.contains("Apollo")){
            System.out.println("They are distracted,lets invade earth!");
        }});
        moon.land("Apollo land");
        moon.land("landing moon");
    }
}
