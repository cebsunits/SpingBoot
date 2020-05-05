package com.tao.hai.util;

import com.alibaba.fastjson.JSONArray;
import com.tao.hai.annotation.FieldQuery;
import com.tao.hai.base.JsonModelBean;
import com.tao.hai.base.ParameterModelBean;
import com.tao.hai.constants.SqlParamConstant;
import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.Transient;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ParameterModelBeanUtil {
    /**
     * 利用反射机制把javabean对象转换为ParameterModelBean对象
     */
    public static <T> ParameterModelBean getParameterModelBean(T t) {
        ParameterModelBean parameterModelBean = new ParameterModelBean();
        JSONArray jsonArray = new JSONArray();
        //
        Class<?> object = t.getClass();
        Field[] fields = object.getDeclaredFields();
        if (fields != null) {
            for (Field field : fields) {
                //字段名称
                String fieldName = field.getName();
                /**跳过忽略部分*/
                Transient transients=field.getAnnotation(Transient.class);
                if(transients!=null){
                    continue;
                }
                /***/
                String query = "";
                /**获取sql查询注解*/
                FieldQuery fieldQuery = field.getAnnotation(FieldQuery.class);
                if (fieldQuery == null) {
                    query = SqlParamConstant.EQUAL;
                } else {
                    query = fieldQuery.value();
                }
                /***/
                PropertyDescriptor propertyDescriptor;
                try {
                    propertyDescriptor = new PropertyDescriptor(fieldName, object);
                } catch (IntrospectionException e) {
                    e.printStackTrace();
                    continue;
                }

                try {
                    Method method = propertyDescriptor.getReadMethod();
                    Object objectValue = method.invoke(t);

                    Class<?> type=propertyDescriptor.getPropertyType();
                    String typeName=type.getSimpleName();
                    if(typeName.equals("Boolean")){
                        if((Boolean)objectValue){
                            objectValue="1";
                        }else{
                            objectValue="0";
                        }
                    }
                    if (ObjectUtils.isNotEmpty(objectValue)) {
                        jsonArray.add(JSONUtils.toJsonObject(new JsonModelBean(fieldName, objectValue.toString(), query)));
                    }


                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }
        parameterModelBean.setQuerystr(jsonArray.toJSONString());
        return parameterModelBean;
    }
}
