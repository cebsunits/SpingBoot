package com.tao.hai.base;

import com.alibaba.fastjson.JSONObject;
import com.tao.hai.json.AjaxJson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BaseController {

    /**
     * 客户端返回JSON字符串
     *
     * @param response
     * @param object
     * @return
     */
    protected String renderString(HttpServletResponse response, Object object) {
        return renderString(response, JSONObject.toJSONString(object));
    }

    /**
     * 客户端返回字符串
     *
     * @param response
     * @param string
     * @return
     */
    protected String renderString(HttpServletResponse response, String string) {
        try {
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    /***
     * 返回json
     * @param response
     * @return
     */
    protected String renderJson(HttpServletResponse response, AjaxJson ajaxJson) {
        String successJsonString = JSONObject.toJSONString(ajaxJson);
        return renderString(response, successJsonString);
    }

}
