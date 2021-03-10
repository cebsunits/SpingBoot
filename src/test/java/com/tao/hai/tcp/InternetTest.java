package com.tao.hai.tcp;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InternetTest {

    public static void main(String[] args) {
        try {
            InetAddress address= InetAddress.getByName("DESKTOP-HTITVG2");

            System.out.println(address);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

}
