package com.tao.hai.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class FileReadNioDemo {
    /**
     * NIO channel方式
     * */
    public void nioCopyFile(String resource,String destination) throws IOException {
        FileInputStream fileInputStream=new FileInputStream(resource);
        FileOutputStream fileOutputStream=new FileOutputStream(destination);
        //读通道
        FileChannel readChannel=fileInputStream.getChannel();
        //写通道
        FileChannel writeChannel=fileOutputStream.getChannel();
        //缓冲区大小
        ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
        boolean isContinue=true;
        while(isContinue){
            //清空缓存
            byteBuffer.clear();
            //读入数据
            int len=readChannel.read(byteBuffer);
            if(len==-1)
                isContinue=false;

            byteBuffer.flip();
            //写入文件
            writeChannel.write(byteBuffer);
        }
        readChannel.close();
        writeChannel.close();
    }
    /**
     * Map channel
     * */
    public void mapChannel(String resoruce,String destination) throws IOException{
        RandomAccessFile resourceFile=new RandomAccessFile(resoruce,"r");
        RandomAccessFile destinationFile=new RandomAccessFile(destination,"rw");
        FileChannel readChannel=resourceFile.getChannel();
        FileChannel writeChannel=destinationFile.getChannel();
        long fileSize=readChannel.size();
        //true 共享锁 false 独占锁 从开始 锁定全部内容 如果获取不到锁会返回null
        FileLock fileLock=readChannel.tryLock(0,fileSize,true);
        if(fileLock!=null){
            MappedByteBuffer mappedByteBuffer=readChannel.map(FileChannel.MapMode.READ_ONLY,0,fileSize);
            byte[] buffer=new byte[(int)fileSize];
            mappedByteBuffer.get(buffer);
            fileLock.release();
            fileLock=writeChannel.tryLock(0,fileSize,false);
            MappedByteBuffer writeByteBuffer=writeChannel.map(FileChannel.MapMode.READ_WRITE,0,fileSize);
            for(int i=0;i<fileSize;i++){
                writeByteBuffer.put(i,buffer[i]);
            }
            writeByteBuffer.flip();
            writeByteBuffer.force();
            fileLock.release();
            //关闭流
            readChannel.close();
            writeChannel.close();
            resourceFile.close();
            destinationFile.close();
        }

    }

    /**普通io操作方式*/
    public void copyFile(String resource,String destination) throws IOException {
        FileInputStream fileInputStream=new FileInputStream(resource);
        FileOutputStream fileOutputStream=new FileOutputStream(destination);
        //创建缓冲流
        BufferedInputStream bis=new BufferedInputStream(fileInputStream);
        //数据缓冲
        byte[] buffer=new byte[1024];
        int length=0;
        while((length=bis.read(buffer))!=-1){
            fileOutputStream.write(buffer);

        }
        bis.close();
        fileInputStream.close();
        fileOutputStream.close();
    }

    public static void main(String[] args) {
        try {
            FileReadNioDemo demo=new FileReadNioDemo();
            long startTime=System.currentTimeMillis();
            String resource="C:\\Users\\cebsu\\Desktop\\金融市场量化交易平台场景.pptx";
            String destination="D:\\金融市场量化交易平台场景.pptx";
            demo.nioCopyFile(resource,destination);
            System.out.println("time "+(System.currentTimeMillis()-startTime));
            startTime=System.currentTimeMillis();
            destination="D:\\金融市场量化交易平台场景IO.pptx";
            demo.copyFile(resource,destination);
            System.out.println("time "+(System.currentTimeMillis()-startTime));

            startTime=System.currentTimeMillis();
            destination="D:\\金融市场量化交易平台场景MAP.pptx";
            demo.mapChannel(resource,destination);
            System.out.println("time "+(System.currentTimeMillis()-startTime));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
