package com.tao.hai.tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class MyChatServer {
    private Vector<Socket> vect=new Vector<>();
    public static final int port=9000;
    public static void main(String[] args) {
        MyChatServer myChatServer=new MyChatServer();
        try {
            myChatServer.startServer(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer(int port) throws IOException {
        ServerSocket serverSocket=new ServerSocket(port);
        System.out.println("服务器端已启动，等待客户端连接！");
        while (true){
            //监听并接收客户端的连接，注意该方法有阻塞性
            Socket s=serverSocket.accept();
            //将客户端的Socket连接添加到一个Vector集合中
            vect.add(s);
            System.out.println("客户端连接成功："+s);
            //打开一个线程，为该客户端的Socket连接提供接收和转发服务
            new MySocketServer(s).start();
        }
    }

    class MySocketServer extends Thread{
        private Socket socket;
        MySocketServer(Socket ss){
            socket=ss;
        }

        @Override
        public void run() {
            try{
                DataInputStream dis=new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                while (true){
                    //等待客户端发送消息
                    String msg=dis.readUTF();
                    System.out.println("服务器端已接收消息:"+msg);
                    //遍历集合中的连接，向外转发消息
                    for(Socket so:vect){
                        if(so!=socket){
                            DataOutputStream dos=new DataOutputStream(new BufferedOutputStream(so.getOutputStream()));
                            dos.writeUTF(msg);
                            dos.flush();
                        }
                    }

                    if("Q".equals(msg)){
                        break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println(socket+"已退出聊天室");
                vect.remove(socket);
            }
        }
    }
}
