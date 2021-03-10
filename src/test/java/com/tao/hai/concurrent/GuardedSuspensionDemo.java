package com.tao.hai.concurrent;


/**
 * 保护暂停：核心思想:仅当服务进程准备好时，才提供服务。
 * */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**请求对象*/
class Request{
    private String name;
    private Data response;//参考future实现，请求返回值
    public synchronized Data getResponse() throws Exception{
        Callable<FutureData> callable=new Callable<FutureData>() {
            @Override
            public FutureData call() throws Exception {
                return (FutureData)response;
            }
        };
        FutureTask<FutureData> futureTask=new FutureTask<>(callable);
        futureTask.run();
        return futureTask.get();
    }
    public synchronized void setResponse(Data response){
        this.response=response;
    }

    public String getName() {
        return name;
    }

    public Request(String name){
        this.name=name;
    }
    public String toString(){
        return "[Request:"+name+"]";
    }
}

interface Data{
    public String getResult();
}
class RequestQueue{
    LinkedList<Request> queue=new LinkedList();

    public synchronized Request getRequest() {
        while(queue.size()==0){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return queue.remove();
    }

    public synchronized void addRequest(Request request) {
        //加入新的请求
        queue.add(request);
        //通知getRequest()方法
        notify();
    }
}
class FutureData implements Data{
    //FutureData是RealData的包装
    RealData realData;
    boolean isReady=false;

    public synchronized void setRealData(RealData realData) {
        if(isReady){
            return;
        }
        this.realData = realData;
        isReady=true;
        //RealData已被注入，通知getResult()
        notifyAll();
    }

    public synchronized String getResult(){
        while (!isReady){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return realData.result;
    }
}
class RealData implements Data,Callable<String> {
    protected final String result;
    //构造，用户可能会等待很久
    public RealData(String name){
        StringBuffer sb=new StringBuffer();
        sb.append(name);
        //模拟很慢过程
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.result=sb.toString();
    }
    public synchronized String getResult(){
        return result;
    }
    @Override
    public String call(){
        //处理真实的业务，并返回
        return "Response:"+result;
    }
}
/**服务端代码*/
class ServerThread extends Thread{
    private RequestQueue requestQueue;

   public ServerThread(RequestQueue requestQueue,String name){
        super(name);
        this.requestQueue=requestQueue;
   }

   public void run(){
       while (true){
           final Request request;
           try {
               request = requestQueue.getRequest();

               final FutureData futureData=(FutureData) request.getResponse();
               //RealData创建耗时
               RealData realData=new RealData(request.getName());
               //处理完成后，通知客户进程
               if(futureData!=null){
                    futureData.setRealData(realData);
               }

             System.out.println(Thread.currentThread().getName()+"handles:"+request);
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   }
}
/**客户端代码*/
class ClientThread extends Thread{
    private RequestQueue requestQueue;
    private List<Request> myRequest=new ArrayList<>();
    public ClientThread(RequestQueue requestQueue,String name){
        super(name);
        this.requestQueue=requestQueue;
    }

    public void run(){
        //提出请求
        for(int i=0;i<10;i++){
            Request request=new Request("RequestID:"+i+" ThreadName:"+Thread.currentThread().getName());
            System.out.println(Thread.currentThread().getName()+"request:"+request);
            //设置一个FutureData对象
            request.setResponse(new FutureData());
            requestQueue.addRequest(request);
            //发送请求
            myRequest.add(request);
            //处理额外的业务，等待服务端装配
            try{
                Thread.sleep(100);
            }catch (Exception e){

            }
            try {
                //取得服务端的返回值
                for(Request r:myRequest){
                    //如果服务没处理完会等待
                    System.out.println("ClientThread Name is:"+Thread.currentThread().getName()+"Response is:"+r.getResponse().getResult());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
public class GuardedSuspensionDemo {

    public static void main(String[] args) {

        RequestQueue requestQueue=new RequestQueue();
        Request request=new Request("name");
        requestQueue.addRequest(request);
        String name="name";
        new Thread(new ServerThread(requestQueue,name)).start();
        new Thread(new ClientThread(requestQueue,name)).start();
    }
}
