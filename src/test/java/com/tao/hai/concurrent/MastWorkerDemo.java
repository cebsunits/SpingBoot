package com.tao.hai.concurrent;

import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Master-Workder 模式的核心思想：由两类进程协作工作：Master进程负责分配和接收任务；Worker任务负责处理子任务；当各个Worker任务完成后将结果返回给Master进行归纳汇总。
 *
 *
 * */
/**Master 处理类*/
class Master{
    //任务队列
    protected Queue<Object> workQueue=new ConcurrentLinkedQueue<>();
    //进程队列
    protected Map<String,Thread> threadMap=new ConcurrentHashMap<>();
    //子任务处理结果集
    protected Map<String,Object> resultMap=new ConcurrentHashMap<>();
    //是否所有的子任务都结束了
    public boolean isComplete(){
        for(Map.Entry<String,Thread> entry:threadMap.entrySet()){
            if(entry.getValue().getState()!=Thread.State.TERMINATED)
                return false;
        }
        return true;
    }
    //Master构造，需要一个Worker进程逻辑，需要的进程数量
    public Master(Worker worker,int countWorker){
        worker.setWorkQueue(workQueue);
        worker.setResultMap(resultMap);
        for(int i=0;i<countWorker;i++){
            threadMap.put(Integer.toString(i),new Thread(worker,Integer.toString(i)));
        }
    }
    //提交任务
    public void submit(Object obj){
        workQueue.add(obj);
    }
    //返回任务结果集
    public Map<String,Object> getResultMap(){
        return resultMap;
    }
    //开始运行所有的Worker进程，进行处理
    public void execute(){
        for(Map.Entry<String,Thread> entry:threadMap.entrySet()){
            entry.getValue().start();
        }
    }
}
/**Worker处理类*/
class Worker implements Runnable{
    //任务队列，获取子任务
    protected Queue<Object> workQueue;
    //子任务处理结果集
    protected Map<String,Object> resultMap;

    public Queue<Object> getWorkQueue() {
        return workQueue;
    }

    public void setWorkQueue(Queue<Object> workQueue) {
        this.workQueue = workQueue;
    }

    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }
    public Object handle(Object obj){
        return obj;
    }
    @Override
    public void run(){
        while(true){
            //获取子任务
            Object input=workQueue.poll();
            if(input==null)
                break;
            //处理子任务
            Object re=handle(input);
            //结果集放入
            resultMap.put(Integer.toString(input.hashCode()),re);
        }
    }
}
class PlusWorker extends Worker{

    public Object handle(Object input){
        Integer i=(Integer)input;
        return i*i*i;
    }
}
public class MastWorkerDemo {

    public static void main(String[] args) {
        Master m=new Master(new PlusWorker(),5);
        //提交100个子任务
        for(int i=0;i<100;i++){
            m.submit(i);
        }
        //开始计算
        m.execute();
        int re=0;
        Map<String,Object> resultMap=m.getResultMap();
        //不用等待所有worker都完成就可以进行计算
        while(resultMap.size()>0||!m.isComplete()){
            Set<String> keys=resultMap.keySet();
            String key=null;
            for(String k:keys){
                key=k;
                break;
            }
            Integer i=null;
            if(key!=null){
                i=(Integer)resultMap.get(key);
            }
            if(i!=null){
                re+=i;
            }
            //移除已经计算过的项
            if(key!=null){
                resultMap.remove(key);
            }
            System.out.println("per cal: "+re);
        }
        System.out.println("end "+re);
    }

}
