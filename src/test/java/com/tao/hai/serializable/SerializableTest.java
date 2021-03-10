package com.tao.hai.serializable;

import java.io.IOException;

public class SerializableTest {
    public static void main(String[] args)  {
        try {
            Person p=new Person();
            p.setAge(15);
            p.setName("zhang");
            p.setIdCard("123123123");
            System.out.println(p.toString());
            //序列化到文件
            String filename="p.txt";
            SerializableUtil.serializable(p,filename);
            //反序列化
            Person dp=(Person) SerializableUtil.deserializable(filename);
            System.out.println("反序列化=="+dp.toString());

            Book book=new Book();
            book.setAuthors("aaa");
            book.setName("金娥");
            book.setPress("人民出版社");
            book.setPrice(35.0);
            System.out.println(book.toString());
            //序列化到文件
            filename="p.txt";
            SerializableUtil.serializable(book,filename);
            //反序列化
            Book bd=(Book) SerializableUtil.deserializable(filename);
            System.out.println("反序列化=="+bd.toString());


            User user=new User();
            user.setAge(15);
            user.setName("zhang");
            user.setIdCard("A123");
            System.out.println(user.toString());
            //序列化到文件
            filename="D:\\p.txt";
            SerializableUtil.serializable(user,filename);
            //反序列化
            User ud=(User) SerializableUtil.deserializable(filename);
            System.out.println("反序列化=="+ud.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }finally {
        }
    }



}
