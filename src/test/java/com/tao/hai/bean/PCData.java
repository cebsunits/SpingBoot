package com.tao.hai.bean;

public class PCData {
    private final int intData;

    public PCData(int d){
        this.intData=d;
    }

    public int getIntData(){
        return intData;
    }
    public String toString(){
        return "Data:="+intData;
    }
}
