package com.tao.hai.concurrent;

import java.util.concurrent.Callable;

public class TaskWithResult implements Callable<String> {
    private int id;

    TaskWithResult(int id){
        this.id=id;
    }

    @Override
    public String call() throws Exception{


        return String.valueOf(id*1000);
    }
}
