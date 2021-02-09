package com.tao.hai.rmi.service;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface PublisherService {

    public void deliverMessage(Serializable message,Serializable topic) throws RemoteException;

}
