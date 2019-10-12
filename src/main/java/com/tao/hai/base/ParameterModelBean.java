package com.tao.hai.base;

import com.alibaba.fastjson.JSONArray;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 参数模板实体bean
 * @author sunits
 * @date 2019/7/18
 */
public class ParameterModelBean {
    private Integer page;//当前页数
    private Integer rows;//每页多少行
    private int draw = 0; //绘制计数器。这个是用来确保Ajax从服务器返回的是对应的（Ajax是异步的，因此返回的顺序是不确定的）。 要求在服务器接收到此参数后再返回
    private String sort;//指定排序的实体类字段
    private String order;//升序asc 降序desc
    private List<JsonModelBean> query;
    private String querystr;

    public ParameterModelBean(Integer page, Integer rows, String sort, String order, String querystr) {
        this.page = page;
        this.rows = rows;
        this.sort = sort;
        this.order = order;
        this.querystr = querystr;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setQuery(List<JsonModelBean> query) {
        this.query = query;
    }

    public String getQuerystr() {
        return querystr;
    }

    public void setQuerystr(String querystr) {
        this.querystr = querystr;
    }

    public List<JsonModelBean> getQuery() {
        if (!StringUtils.isEmpty(querystr)) {
            try {
                query = JSONArray.parseArray(querystr, JsonModelBean.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return query;
    }

}
