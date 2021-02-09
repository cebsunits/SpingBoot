package com.tao.hai.bean;

/**
 * 枚举类创建单实例对象
 */
public enum EnumSingleton {
    INSTANCE;

    public EnumSingleton getInstance() {
        return INSTANCE;
    }

}

class SingletonTest {
    private SingletonTest() {

    }

    enum EnumSingleton {
        INSTANCE;
        private SingletonTest singletonTest;

        private EnumSingleton() {
            singletonTest = new SingletonTest();
        }
        public SingletonTest getInstance(){
            return singletonTest;
        }
    }
    //对外暴露一个获取User对象的静态方法
    public static SingletonTest getInstance(){
        return EnumSingleton.INSTANCE.getInstance();
    }
}

class test {
    public static void main(String[] args) {
        SingletonTest singleton1 = SingletonTest.getInstance();
        SingletonTest singleton2 =  SingletonTest.getInstance();
        System.out.println("singleton1 = [" + singleton1 + "]");
        System.out.println("singleton2 = [" + singleton2 + "]");

        /* 问题分析         *
         枚举类底层其实也是class, 集成了 Enum
        * * idea 编译结果 和 javap -p xxx.class 反编译,也是 无惨的私有构造, 欺骗了我们
        * * 使用jad 工具, 底层其实是 有参的 私有构造 参数为 String , int 俩个
        * * 结果得到了 我们的 异常 Cannot reflectively create enum objects
        * * declaredConstructor.newInstance(); 源码分析 为 : 为枚举类的时候, 不能create enum objects */
    }
}