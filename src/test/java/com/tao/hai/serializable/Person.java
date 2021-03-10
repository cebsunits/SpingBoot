package com.tao.hai.serializable;

import java.io.Serializable;
/**序列化時需要继承Serializable接口
 * 如果要部分序列化：
 *  1）添加transient关键字
 *  2)添加WriteObect()和readObject()方法
 * */
public class Person implements Serializable {

    private String name;

    private int age;

    /**身份证不被序列化*/
    private transient String idCard;

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

    @Override
    public String toString() {
        return "name["+name+"]age["+age+"]idCard["+idCard+"]";
    }
}
