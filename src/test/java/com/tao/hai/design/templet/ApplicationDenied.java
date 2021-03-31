package com.tao.hai.design.templet;

import org.springframework.web.client.HttpClientErrorException;

public class ApplicationDenied extends Exception {


    public ApplicationDenied(String message){
        super(message);
    }

}
