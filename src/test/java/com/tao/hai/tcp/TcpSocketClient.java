package com.tao.hai.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class TcpSocketClient {
    public static final int port=9090;
    public static final String host="localhost";

    public static void main(String[] args) {
        System.out.println("Client start ****");
        while (true){
            Socket socket=null;
            try{
                //创建一个连接
                socket=new Socket(host,port);
                //如果要设置超时，单位毫秒，需要捕获InterruptedException异常
                //socket.setSoTimeout(1000);
                //读取服务器端数据
                BufferedReader input=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //向服务器端发送数据
                PrintStream out=new PrintStream(socket.getOutputStream());
                System.out.println("请输入：\t");
                String line=new BufferedReader(new InputStreamReader(System.in)).readLine();
                out.println(line);

                String ret=input.readLine();
                System.out.println("服务器端返回来的数据是:"+ret);
                if(ret==null){
                    break;
                }
                //接收到OK断开连接
                if("OK".equals(ret)){
                    System.out.println("客户端将关闭连接");
                    Thread.sleep(50);
                    break;
                }
                out.close();
                input.close();
            }catch (Exception e){
                System.out.println("客户端 run 异常:"+e.getMessage());
            }finally {
                if(socket!=null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        System.out.println("客户端 finally 异常:"+e.getMessage());
                    }
                }
            }

        }

    }

}
