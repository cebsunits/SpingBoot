package com.tao.hai.rmi.service;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PublisherRemoteService extends Remote {

    public static final String PUBLISHER_NAME="publisher";

    public void addSuscriber(SubscrberRemoteService subscrber, Serializable topic) throws RemoteException;

    public void removeSubscriber(SubscrberRemoteService subscrber, Serializable topic)throws RemoteException;
}
