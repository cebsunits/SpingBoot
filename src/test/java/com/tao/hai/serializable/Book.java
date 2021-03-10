package com.tao.hai.serializable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Book implements Externalizable {
    private String name;
    private String authors;
    private String press;
    private double price;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "[book [name="+name+",press="+press+",authors="+authors+",price="+price+"]]";
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
        out.writeObject(press);
        out.writeObject(authors);
        out.writeDouble(price);
    }

    /**必須按順序读取，如果不一致则会抛出异常java.io.OptionalDataException或者获取不到对应的值*/
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name=(String)in.readObject();
        press=(String)in.readObject();
        authors=(String)in.readObject();
        price=in.readDouble();
    }
}
