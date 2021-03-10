package com.tao.hai.serializable;

import java.io.*;
/**
 * 序列化与反序列化工具类
 *
 * */
public class SerializableUtil {

    /**序列化*/
    public static void serializable(Object obj,String fileName) throws IOException {
        FileOutputStream fos=new FileOutputStream(fileName);
        BufferedOutputStream bos=new BufferedOutputStream(fos);
        ObjectOutputStream oos=new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.close();
    }
    /**反序列化*/
    public static Object deserializable(String fileName)throws IOException,ClassNotFoundException{
        FileInputStream fis=new FileInputStream(fileName);
        BufferedInputStream bis=new BufferedInputStream(fis);
        ObjectInputStream ois=new ObjectInputStream(bis);
        Object object=ois.readObject();
        return object;
    }
}
