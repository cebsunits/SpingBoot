package com.tao.hai.proxy;

import javassist.*;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;
import org.springframework.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;

public class ProxyUse {
    public static void main(String[] args) {
        //使用代理
        IDBQuery query=new DBQueryProxy();
        //在真正使用时创建真实对象
        query.request();
    }
    /**动态代理*/
    public static IDBQuery createJDKProxy(){
        IDBQuery jdkProxy= (IDBQuery)Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),new Class[]{IDBQuery.class},new JdkDbQueryHandler());
        return jdkProxy;
    }
    /**CGlib动态代理*/
    public static IDBQuery createCglibProxy(){
        Enhancer enhancer=new Enhancer();
        //指定切入器，定义代理类逻辑
        enhancer.setCallback(new CglibDBQueryInterceptor());
        //指定实现的接口
        enhancer.setInterfaces(new Class[]{IDBQuery.class});
        //生成代理类的实例
        IDBQuery real=(IDBQuery)enhancer.create();
        return real;
    }
    /**javasist 动态代理*/
    public static IDBQuery createJavasistProxy() throws Exception{
        ProxyFactory proxyFactory=new ProxyFactory();
        //指定接口
        proxyFactory.setInterfaces(new Class[]{IDBQuery.class});
        Class proxyClasses=proxyFactory.createClass();
        IDBQuery javasistProxy=(IDBQuery)proxyClasses.newInstance();
        //设置handler处理器
        ((ProxyObject)javasistProxy).setHandler(new JavasistDynDBQueryHandler());
        return javasistProxy;
    }
    /**javasist byte code dynmic proxy*/
    public static IDBQuery createJavasistByteCodeProxy() throws Exception{
        ClassPool classPool=new ClassPool(true);
        //定义类名
        CtClass mCtc=classPool.makeClass(IDBQuery.class.getName()+"JavasistByteCodeProxy");
        //需要实现的接口
        mCtc.addInterface(classPool.get(IDBQuery.class.getName()));
        //添加构造函数
        mCtc.addConstructor(CtNewConstructor.defaultConstructor(mCtc));
        //添加类的字段信息，使用动态Java代码
        mCtc.addField(CtField.make("public "+IDBQuery.class.getName()+" real;",mCtc));
        String dbqueryName=DBQuery.class.getName();
        //添加方法，这里使用动态JAVA代码指定内部逻辑
        mCtc.addMethod(CtMethod.make("public String request(){if(real==null){real=new "+dbqueryName+";}return real.request();}",mCtc));
        //基于以上信息，生成动态类
        Class pc=mCtc.toClass();
        //生成动态类的实例
        IDBQuery byteCodeProxy=(IDBQuery) pc.newInstance();
        return byteCodeProxy;
    }
}
