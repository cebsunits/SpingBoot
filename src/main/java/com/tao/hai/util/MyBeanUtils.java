package com.tao.hai.util;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;

public class MyBeanUtils extends PropertyUtilsBean {

    /**
     * @param source 源对象
     * @param target 目的对象
     * @description 从源对象把相应的值拷贝的目的对象中
     * */
    public static void convert(Object source,Object target){
        if(source==null){
            throw new IllegalArgumentException("No source bean specified!");
        }
        if(target==null){
            throw new IllegalArgumentException("No target bean specified!");
        }
        //DanyBean 提供了可以动态修改实现他的类的属性名称、属性值、属性类型的功能
        if(source instanceof DynaBean){
            DynaProperty[] dynaProperties=((DynaBean) source).getDynaClass().getDynaProperties();
            if(dynaProperties!=null){
                for(int i=0;i<dynaProperties.length;i++){
                    DynaProperty dynaProperty= dynaProperties[i];
                    String name=dynaProperty.getName();
                    Object value= ((DynaBean) source).get(name);
                    setTargetValue(target,name,value);
                }
            }
        }else if(source instanceof Map){
            /**如果源对象是map*/
            Iterator names=((Map) source).keySet().iterator();
            while(names.hasNext()){
                String name= (String) names.next();
                Object value= ((DynaBean) source).get(name);
                setTargetValue(target,name,value);
            }
        }else{
            /**正常的java bean对象*/
            PropertyDescriptor[] propertyDescriptors=PropertyUtils.getPropertyDescriptors(source);
            for(int i=0;i<propertyDescriptors.length;i++){
                PropertyDescriptor propertyDescriptor=propertyDescriptors[i];
                String name =propertyDescriptor.getName();
                if("class".equals(name)){
                    continue;
                }
                Object value= ((DynaBean) source).get(name);
                setTargetValue(target,name,value);
            }
        }
    }

    /**对新对象相同的bean name进行赋值*/
    public static void setTargetValue(Object target,String name,Object value){
        if(PropertyUtils.isWriteable(target,name)){
            try {
                getInstance().setSimpleProperty(target,name,value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param source 源对象
     * @param target 目的对象
     * @description 从源对象把相应的值拷贝的目的对象中
     * */
    public static void copyBean2Bean(Object source, Object target) throws Exception {
        convert(source, target);
    }
    /**
     * @param map map信息
     * @param target 目的对象
     * @description 从map信息把相应的值拷贝的目的对象中
     * */
    public static void copyMap2Bean(Map map, Object target) throws Exception {
       if(map==null || target==null){
           throw new IllegalArgumentException("Map is null or target is null,it`s can`t copy!");
       }
       Iterator iterator=map.keySet().iterator();
       while(iterator.hasNext()){
           String name= (String) iterator.next();
           if(StringUtils.isEmpty(name)){
               continue;
           }
           Object value=map.get(name);

           try {
               Class classz=PropertyUtils.getPropertyType(target,name);
                if(null==classz){
                    continue;
                }
                String className=classz.getName();
                if("java.sql.Timestamp".equalsIgnoreCase(className)){
                    if(value==null||value.equals("")){
                        continue;
                    }
                }
                getInstance().setSimpleProperty(target,name,value);
           } catch (IllegalAccessException e) {
               e.printStackTrace();
           } catch (InvocationTargetException e) {
               e.printStackTrace();
           } catch (NoSuchMethodException e) {
               e.printStackTrace();
           }
       }
    }
    /**
     * @param source 源对象
     * @param map map信息
     * @description 从源对象把相应的值拷贝到map中
     * */
    public static void copyBean2Map(Object source,Map map){
        if(map==null || source==null){
            throw new IllegalArgumentException("Map is null or target is null,it`s can`t copy!");
        }
        PropertyDescriptor[] propertyDescriptors=PropertyUtils.getPropertyDescriptors(source);
        for(int i=0;i<propertyDescriptors.length;i++){
            PropertyDescriptor propertyDescriptor=propertyDescriptors[i];
            String name=propertyDescriptor.getName();
            try {
                Object value=PropertyUtils.getSimpleProperty(source,name);
                map.put(name,value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}
