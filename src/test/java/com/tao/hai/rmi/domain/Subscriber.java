package com.tao.hai.rmi.domain;

import com.tao.hai.rmi.bean.MessageEvent;
import com.tao.hai.rmi.bean.MessageListener;
import com.tao.hai.rmi.service.PublisherRemoteService;
import com.tao.hai.rmi.service.SubscrberRemoteService;
import com.tao.hai.rmi.service.SubscriberService;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class Subscriber extends UnicastRemoteObject implements SubscriberService, SubscrberRemoteService {

    private HashMap listeners;

    private PublisherRemoteService publisher;

    protected Subscriber(String publisherHost) throws RemoteException, MalformedURLException, NotBoundException {
        listeners=new HashMap();
        String urlName= File.separator+File.separator+publisherHost+File.separator+PublisherRemoteService.PUBLISHER_NAME;
        publisher=(PublisherRemoteService) Naming.lookup(urlName);
    }

    @Override
    public void deliverMessage(Serializable message, Serializable topic) throws RemoteException {
        ArrayList arrayList;
        arrayList=(ArrayList)listeners.get(topic);
        if(arrayList!=null&&arrayList.size()>0){
            MessageEvent evt;
            evt=new MessageEvent(this,message,topic);
            for(int i=0;i<arrayList.size();i++){
                MessageListener listener;
                listener=(MessageListener)arrayList.get(i);
                listener.receiverMessage(evt);
            }
        }
    }

    @Override
    public void addMessageListener(MessageListener listener, Serializable topic) throws RemoteException, NotBoundException {
        ArrayList arrayList;
        arrayList=(ArrayList)listeners.get(topic);
        if(arrayList==null){
            arrayList=new ArrayList();
            listeners.put(topic,arrayList);
        }
        if(arrayList.size()==0){
            subscribe(topic);
        }
        arrayList.add(listener);
    }

    private void subscribe(Serializable topic) throws RemoteException{
        publisher.addSuscriber(this,topic);
    }
    private void unSubscriber(Serializable topic) throws RemoteException{
        publisher.removeSubscriber(this,topic);
    }
    @Override
    public void removeMessageListener(MessageListener listener, Serializable topic) throws RemoteException, NotBoundException {
        ArrayList arrayList;
        arrayList=(ArrayList)listeners.get(topic);
        if(arrayList!=null&&arrayList.size()>0){
            for(int i=0;i<arrayList.size();i++){
                int index=arrayList.indexOf(listener);
                if(index>0){
                    arrayList.remove(index);
                    if(arrayList.size()==0){
                        unSubscriber(topic);
                    }
                }
            }
        }

    }
}
