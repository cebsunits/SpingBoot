package com.tao.hai.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
/**
 *
 * 懒汉式创建单实例对象
 * */
public class LanHanSingleton {
    //增加标志位 , 防止反射 破坏
    private static boolean flag = false;

    // 保证私有,只有一个
    private LanHanSingleton() {
        synchronized (LanHanSingleton.class) {
            if (!flag) {
                flag = true;
            } else {
                throw new RuntimeException("不要试图使用反射破坏 异常");
            }
        }
    }

    // 避免指令重排
    private volatile static LanHanSingleton lazyMan = null;

    //多线程下 是不安全的 
    public static LanHanSingleton getInstance() {
        if (lazyMan == null) {
            lazyMan = new LanHanSingleton();
        }
        return lazyMan;
    }

    // “双重校验锁”实现单例
    public static LanHanSingleton getInstance2() {
        if (lazyMan == null) {
            synchronized (LanHanSingleton.class) {
                if (lazyMan == null) {
                    // 并不是一个原子操作  
                    lazyMan = new LanHanSingleton();
                    // 可能会出现指令重排  
                    /* * 执行流程 * 1、分配内存空间 * 2、执行构造方法，初始化对象 * 3、把这个对象执行这个空间 * * 123 正常执行 * 132 指令重排 * A 线程 执行到了3 * B 线程进来，此时 lazyMan 未执行 new 初始化操作2， 发生了指令重排， 此地址空间下的对象为空 ， 不安全 * 添加 volatile ，避免 指令重排 */
                }
            }
        }
        return lazyMan;
    }

    // 反射  对象被破坏 破坏单例
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        Field flag = LanHanSingleton.class.getDeclaredField("flag");
        flag.setAccessible(true);
        Constructor<LanHanSingleton> declaredConstructor = LanHanSingleton.class.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        LanHanSingleton instance1 = declaredConstructor.newInstance();
        System.out.println(instance1);
        // 道高一尺魔高一丈  修改 flag的值  
        flag.set(instance1, false);
        LanHanSingleton instance = LanHanSingleton.getInstance2();
        System.out.println(instance);
        Constructor<LanHanSingleton> declaredConstructor2 = LanHanSingleton.class.getDeclaredConstructor();
        declaredConstructor2.setAccessible(true);
        LanHanSingleton instance3 = declaredConstructor2.newInstance();
        System.out.println(instance3);
    }
}

