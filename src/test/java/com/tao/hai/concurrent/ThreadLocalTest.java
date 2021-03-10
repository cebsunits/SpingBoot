package com.tao.hai.concurrent;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
/**
 * 通过ThreadLocal进行同步
 *
 * */
public class ThreadLocalTest {

    public static final SessionFactory sessionFactory;
    static {
        sessionFactory=new Configuration().configure().buildSessionFactory();
    }
    public static final ThreadLocal session=new ThreadLocal();
    public static Session currentSession() throws HibernateException {
        Session s=(Session)session.get();
        if(s==null){
            s=sessionFactory.openSession();
            session.set(s);
        }
        return s;
    }

    public static void closeSession() throws HibernateException{
        Session s=(Session)session.get();
        if(s!=null){
            s.close();
        }
        session.set(null);
    }
}
