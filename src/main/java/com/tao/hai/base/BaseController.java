package com.tao.hai.base;

import com.alibaba.fastjson.JSONObject;
import com.tao.hai.json.AjaxJson;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
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

    /***获取分页信息*/
    public ParameterModelBean parmeterPage(HttpServletRequest request) {

        String pageSizeStr = request.getParameter("pageSize");
        String pageNumberStr = request.getParameter("pageNumber");
        Integer pageSize = 10;
        Integer pageNumber = 0;
        if (StringUtils.isNotEmpty(pageSizeStr) && StringUtils.isNotEmpty(pageNumberStr)) {
            try {
                pageSize = Integer.parseInt(pageSizeStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                pageNumber = Integer.parseInt(pageNumberStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ParameterModelBean bean = new ParameterModelBean();
        bean.setPage(pageNumber);
        bean.setRows(pageSize);
        return bean;
    }
}
