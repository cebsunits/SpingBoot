package com.tao.hai.serializable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**序列化時需要继承Serializable接口
 * 如果要部分序列化：
 *  1）添加transient关键字
 *  2)添加WriteObect()和readObject()方法
 * */
public class User implements Serializable {

    private String name;

    private int age;

    /**身份证不被序列化*/
    private String idCard;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    /**自定义序列化*/
    public void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(name);
        out.writeInt(age);
        String card=reverse(idCard);
        out.writeObject(card);
    }
    /**自定义序列化*/
    public void readObject(ObjectInputStream in) throws IOException ,ClassNotFoundException{
        name=(String)in.readObject();
        age=in.readInt();
        idCard=(String)in.readObject();
    }
    public String reverse(String in){
        StringBuffer sb=new StringBuffer(in);
        sb.reverse();
        return sb.toString();
    }
    @Override
    public String toString() {
        return "name["+name+"]age["+age+"]idCard["+idCard+"]";
    }
}
