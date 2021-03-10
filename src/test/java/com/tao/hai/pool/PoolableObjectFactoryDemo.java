package com.tao.hai.pool;


import org.apache.commons.pool2.DestroyMode;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.concurrent.atomic.AtomicInteger;

/**对重量级对象进行池化管理可以提高性能，轻量级的会降低*/
public class PoolableObjectFactoryDemo implements PooledObjectFactory {
    private static AtomicInteger counter=new AtomicInteger(0);
    @Override
    public PooledObject makeObject() throws Exception {
        Object obj=counter.getAndIncrement();
        System.out.println("create object="+obj);
        return new DefaultPooledObject(obj);
    }

    @Override
    public void destroyObject(PooledObject pooledObject) throws Exception {

    }

    @Override
    public void destroyObject(PooledObject p, DestroyMode mode) throws Exception {
        System.out.println("destroying object "+p);
    }

    @Override
    public boolean validateObject(PooledObject pooledObject) {
        return false;
    }

    @Override
    public void activateObject(PooledObject pooledObject) throws Exception {
        //取出前调用
        System.out.println("before borrow="+pooledObject);
    }

    @Override
    public void passivateObject(PooledObject pooledObject) throws Exception {
        //对象返回池中调用
        System.out.println("return "+pooledObject);
    }
}
