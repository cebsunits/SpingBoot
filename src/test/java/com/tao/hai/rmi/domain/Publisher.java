package com.tao.hai.rmi.domain;

import com.tao.hai.rmi.service.PublisherRemoteService;
import com.tao.hai.rmi.service.PublisherService;
import com.tao.hai.rmi.service.SubscrberRemoteService;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class Publisher extends UnicastRemoteObject implements PublisherService, PublisherRemoteService {

    private HashMap subscribers;
    protected Publisher() throws RemoteException,MalformedURLException, AlreadyBoundException{
        Naming.bind(PUBLISHER_NAME,this);
    }

    @Override
    public void addSuscriber(SubscrberRemoteService subscrber, Serializable topic) throws RemoteException {
        ArrayList subscriberList;
        subscriberList=(ArrayList)subscribers.get(topic);
        if(subscriberList==null){
            subscriberList=new ArrayList();
            subscribers.put(topic,subscriberList);
        }
        subscriberList.add(subscrber);
    }

    @Override
    public void removeSubscriber(SubscrberRemoteService subscrber, Serializable topic) throws RemoteException {
        ArrayList subscriberList;
        subscriberList=(ArrayList)subscribers.get(topic);
        if(subscriberList!=null&&subscriberList.size()>0){
            int index=subscriberList.indexOf(subscrber);
            if(index>0){
                subscriberList.remove(index);
            }
        }
    }

    @Override
    public void deliverMessage(Serializable message, Serializable topic) throws RemoteException {
        ArrayList subscriberList;
        subscriberList=(ArrayList)subscribers.get(topic);
        if(subscriberList!=null&&subscriberList.size()>0){
            for(int i=0;i<subscriberList.size();i++){
                SubscrberRemoteService subscriber;
                subscriber=(SubscrberRemoteService)subscriberList.get(i);
                subscriber.deliverMessage(message,topic);
            }
        }
    }
}
