package com.tao.hai.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
/**
 *
 * 采用多线程方式处理连接信息
 * 如果传递中文：
 *  1）DataOutputStream.writeUTF()方法发送消息，DataInputStream.readUTF()读取消息
 *  2）使用Write字符流，OutputStreamWriter\PrintWriter
 * 传递对象：ObjectInputStream和ObjectOutputStream
 * */
public class TcpSockerServer {

    public static void main(String[] args) {
        try {
            System.out.println("Server start ****");
            //创建连接
            int port=9090;
            ServerSocket serverSocket=new ServerSocket(port);
            while (true){
                //取出一个连接
                Socket socket=serverSocket.accept();
                new HanlerThread(socket);
            }
        } catch (IOException e) {
            System.out.println("服务器异常: " + e.getMessage());
        }
    }
}


class HanlerThread implements Runnable{
    private Socket socket;
    HanlerThread(Socket sk){
        socket=sk;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try{
            //读取客户端数据
            BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //需要和客户端输出流的写方法对应，否则会抛出EOFException
            String clientInputStr=reader.readLine();
            //处理客户端数据
            System.out.println("客户端发过来的内容:" + clientInputStr);
            //向客户端回复消息
            PrintStream out=new PrintStream(socket.getOutputStream());
            System.out.println("请输入：\t");
            //发送键盘输入的一行信息
            String line=new BufferedReader(new InputStreamReader(System.in)).readLine();
            out.println(line);
            out.close();
            reader.close();
        }catch (Exception e){
            System.out.println("服务器 run 异常:"+e.getMessage());
        }finally {
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("服务器 finally 异常:"+e.getMessage());
                }
            }
        }
    }
}