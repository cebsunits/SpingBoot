package com.tao.hai.util;

import com.github.pagehelper.util.StringUtil;
import com.tao.hai.base.JsonModelBean;
import com.tao.hai.base.ParameterModelBean;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;

/**
 * 组装查询条件工具类
 */
public class ExampleUtil {
    public static<T> Example getExample(Class<T> clazz, ParameterModelBean parameterModel) {
        Example example = example = new Example(clazz);
        if (parameterModel != null) {
            List<JsonModelBean> list = parameterModel.getQuery();
            String sort = parameterModel.getSort();
            String order = parameterModel.getOrder();
            Example.Criteria criteria = example.createCriteria();
            if (list != null && list.size() > 0) {
                for (JsonModelBean jsonModel : list) {
                    //实体类是否存在该字段且字段值不为空
                    if (jsonModel.getName() != null && jsonModel.getType() != null && StringUtil.isNotEmpty(jsonModel.getValue())  && existsField(clazz, jsonModel.getName())) {
                        if ("llike".equals(jsonModel.getType()) ) {
                            criteria.andLike(jsonModel.getName(), "%"+jsonModel.getValue());
                            continue;
                        }
                        if ("rlike".equals(jsonModel.getType()) ) {
                            criteria.andLike(jsonModel.getName(), jsonModel.getValue()+"%");
                            continue;
                        }
                        if ("like".equals(jsonModel.getType()) ) {
                            criteria.andLike(jsonModel.getName(), "%"+jsonModel.getValue()+"%");
                            continue;
                        }
                        if ("eq".equals(jsonModel.getType())) {
                            criteria.andEqualTo(jsonModel.getName(), jsonModel.getValue());
                            continue;
                        }
                        if ("lt".equals(jsonModel.getType())) {
                            criteria.andLessThan(jsonModel.getName(), jsonModel.getValue());
                            continue;
                        }
                        if ("le".equals(jsonModel.getType())) {
                            criteria.andLessThanOrEqualTo(jsonModel.getName(), jsonModel.getValue());
                            continue;
                        }
                        if ("gt".equals(jsonModel.getType())) {
                            criteria.andGreaterThan(jsonModel.getName(), jsonModel.getValue());
                            continue;
                        }
                        if ("ge".equals(jsonModel.getType())) {
                            criteria.andGreaterThanOrEqualTo(jsonModel.getName(), jsonModel.getValue());
                            continue;
                        }
                        if ("in".equals(jsonModel.getType())) {
                            criteria.andIn(jsonModel.getName(), getList(jsonModel.getValue()));
                            continue;
                        }
                    }
                }
            }

            if (sort != null) {
                if ("asc".equals(order)) {
                    example.orderBy(sort).asc();
                }
                if ("desc".equals(order)) {
                    example.orderBy(sort).desc();
                }
            }
        }


        return example;
    }

    /**
     * 判断该实例是否存在该字段
     * @param clz
     * @param fieldName
     * @return
     */
    private static boolean existsField(Class clz,String fieldName){
        try{
            return clz.getDeclaredField(fieldName)!=null;
        }
        catch(Exception e){
        }
        if(clz!=Object.class){
            return existsField(clz.getSuperclass(),fieldName);
        }
        return false;
    }

    /**
     * 将字符串转换为集合
     * @param string
     * @return
     */
    private static List<String> getList(String string) {
        String[] split = string.split(",");
        List<String> strings = Arrays.asList(split);
        return strings;
    }

}
