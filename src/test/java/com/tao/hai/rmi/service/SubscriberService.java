package com.tao.hai.rmi.service;


import com.tao.hai.rmi.bean.MessageListener;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public interface SubscriberService {

    public void addMessageListener(MessageListener listener, Serializable topic)throws RemoteException, NotBoundException;

    public void removeMessageListener(MessageListener listener, Serializable topic)throws RemoteException, NotBoundException;
}
