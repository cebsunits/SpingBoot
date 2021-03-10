package com.tao.hai.tcp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class MyChatClient {
    JFrame jf=new JFrame("聊天客户端");
    JTextArea jt=new JTextArea();
    DataOutputStream dos=null;
    JTextField jtf=new JTextField(15);

    public static void main(String[] args) {
        MyChatClient chatClient=new MyChatClient();
        try {
            chatClient.createForm();
            chatClient.clientStart("localhost",9000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createForm(){
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.getContentPane().add(jt, BorderLayout.CENTER);
        JButton jb=new JButton("发送");
        SendButtonAction sendButtonAction=new SendButtonAction();
        jb.addActionListener(sendButtonAction);
        JPanel jp=new JPanel();
        jp.add(jtf);
        jp.add(jb);
        jf.getContentPane().add(jp,BorderLayout.SOUTH);
        jf.setSize(300,300);
        jf.setVisible(true);
    }
    public void clientStart(String host,int port) throws IOException{
        Socket socket=new Socket(host,port);
        //将发消息的输出流实例化，为后续发送消息做准备
        dos=new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        //打开一个接收消息的线程
        new Thread(new MySocketReadServer(socket)).start();
    }

    class SendButtonAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String msg=jtf.getText();
            jtf.setText("");
            if("".equals(msg)){
                JOptionPane.showMessageDialog(jf,"发送的内容不能为空");
                return;
            }
            try {
                dos.writeUTF(msg);
                dos.flush();
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(jf,"发送失败，请重新尝试");
                e1.printStackTrace();
            }
        }
    }

    class MySocketReadServer implements Runnable{
        private Socket s;
        MySocketReadServer(Socket so){
            s=so;
        }

        @Override
        public void run() {
            try{
                DataInputStream in=new DataInputStream(new BufferedInputStream(s.getInputStream()));
                while (true){
                    String msg=jt.getText()+"\n\r"+in.readUTF();
                    jt.setText(msg);
                    if("Q".equals(msg)){
                        break;
                    }
                }
            }catch (Exception e){
                System.out.println("已退出聊天室："+e.getMessage());
            }
        }
    }
}

