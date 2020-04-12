package com.tao.hai.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JSONUtils {

    /**
     * 对象转json字符串
     *
     * @param obj
     * @return
     */
    public static String toJsonString(Object obj) {
        return JSON.toJSONString(obj);
    }

    /***
     * 对象转JSON
     * @param obj
     * @return
     */
    public static Object toJsonObject(Object obj) {
        return JSON.toJSON(obj);
    }

    /***
     * JSONObject转换为JavaObject
     * @param json,javaClass
     * @return
     */
    public static Object parseObject(String string, Class javaClass) {
        return JSON.parseObject(string, javaClass);
    }

    /**
     * 将字符串转为JSONObject
     *
     * @param string
     * @return
     */
    public static JSONObject parseObject(String string) {
        return JSON.parseObject(string);
    }
}
