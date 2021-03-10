package com.tao.hai.bean;

import java.util.Arrays;

public class RewriteEqualsMethod {

    private String name;
    private int age;
    private long card;
    private float price;
    private double avePrice;
    private String[] lists;

    @Override
    public boolean equals(Object o){
        if(o==null)
            return false;
        if(o.getClass()!=getClass())
            return false;
        RewriteEqualsMethod m=(RewriteEqualsMethod)o;
        return m.name.equals(name)&&m.age==age&&m.card==card&&Float.floatToIntBits(m.price)==Float.floatToIntBits(price)&&
                Double.doubleToLongBits(m.avePrice)==Double.doubleToLongBits(avePrice)&&listsEquals(m.lists);
    }
    @Override
    public int hashCode(){
        int result=17;
        result=result*37+name.hashCode();
        result=result*37+age;
        result=result*37+(int)(card^(card>>>32));
        result=result*37+Float.floatToIntBits(price);
        long al=Double.doubleToLongBits(avePrice);
        result=result*37+(int)(al^(al>>>32));
        result=result*37+listsHashCode(lists);
        return result;
    }
    private boolean listsEquals( String[] l){
        return Arrays.equals(l,lists);
    }
    private int listsHashCode( String[] l){
        int result=17;
        for(int i=0;i<l.length;i++){
            result=result*37+l[i].hashCode();
        }
        return result;
    }
}
