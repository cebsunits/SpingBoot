package com.tao.hai.rmi.service;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SubscrberRemoteService extends Remote {

    public void deliverMessage(Serializable message, Serializable topic)throws RemoteException;
}
