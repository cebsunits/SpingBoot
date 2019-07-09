package com.tao.hai.base;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

public class DataTablePage<T> implements Serializable {
    //绘制计数器。这个是用来确保Ajax从服务器返回的是对应的（Ajax是异步的，因此返回的顺序是不确定的）。 要求在服务器接收到此参数后再返回
    private int draw = 0;
    //第一条数据的起始位置，比如0代表第一条数据
    private int start = 0;
    //每页的长度
    private int length = 20;
    //全局的搜索条件，条件会应用到每一列（ searchable需要设置为 true ）
    private String search;
    /*
     * 如果为 true代表全局搜索的值是作为正则表达式处理，为 false则不是。
     * 注意：通常在服务器模式下对于大数据不执行这样的正则表达式，但这都是自己决定的
     */
    private boolean isSearch;

    /*
     * 告诉后台那些列是需要排序的。 i是一个数组索引，对应的是 columns配置的数组，从0开始
     */
    private int[] order;

    /*
     * 告诉后台列排序的方式， desc 降序 asc升序
     */
    private String orderDir;
    /*
     * columns 绑定的数据源，由 columns.dataOption 定义。
     */
    private String columnsData;

    /*
     * columns 的名字，由 columns.nameOption 定义。
     */
    private String columnsName;

    /*
     * 标记列是否能被搜索,为true代表可以，否则不可以，这个是由 columns.searchableOption 控制
     */
    private String columnsSearchable;

    /*
     * 标记列是否能排序,为 true代表可以，否则不可以，这个是由 columns.orderableOption 控制
     */
    private boolean isOrderable;

    /*
     * 标记具体列的搜索条件
     */
    private String columnsSearchValue;
    /*
     * 特定列的搜索条件是否视为正则表达式， 如果为 true代表搜索的值是作为正则表达式处理，为 false则不是。
     * 注意：通常在服务器模式下对于大数据不执行这样的正则表达式，但这都是自己决定的
     */
    private boolean isSearchRegex;

    /*
     * 必要。即没有过滤的记录数（数据库里总共记录数）
     */
    private int recordsTotal;
    /*
     * 必要。过滤后的记录数（如果有接收到前台的过滤条件，则返回的是过滤后的记录数）
     */
    private int recordsFiltered;
    /*
     * 必要。表中中需要显示的数据。这是一个对象数组，也可以只是数组， 区别在于 纯数组前台就不需要用 columns绑定数据，会自动按照顺序去显示
     * ，而对象数组则需要使用 columns绑定数据才能正常显示。 注意这个 data的名称可以由 ajaxOption 的
     * ajax.dataSrcOption 控制
     */
    private List<T> data;

    /*
     * 可选。你可以定义一个错误来描述服务器出了问题后的友好提示
     */
    private String error;
    /*-------------可选参数-----------------*/
    /*
     * 自动绑定到 tr节点上
     */
    private String dtRowId;

    /*
     * 自动把这个类名添加到 tr
     */
    private String dtRowClass;

    /*
     * 使用 jQuery.data() 方法把数据绑定到row中，方便之后用来检索（比如加入一个点击事件）
     */
    private Object dtRowData;

    /*
     * 自动绑定数据到 tr上，使用 jQuery.attr() 方法，对象的键用作属性，值用作属性的值。 注意这个 需要 Datatables
     * 1.10.5+的版本才支持
     */
    private Object dtRowAttr;

    /*-------------可选参数-----------------*/
    /*------------------服务器需要返回的数据(Returned data) end--------------------*/

    /*
     * 当前页码
     */
    private int pageNum = 1;

    /*
     * 每页数据
     */
    private int pageSize = 100;

    public DataTablePage() {

    }

    /**
     * 获取页面信息
     */
    public DataTablePage(HttpServletRequest request) {
        //开始的数据行数
        String start = request.getParameter("start");
        if (StringUtils.isEmpty(start)) {
            start = Integer.toString(this.start);
        }
        //每页的数据数
        String length = request.getParameter("length");
        if (StringUtils.isEmpty(length)) {
            length = Integer.toString(this.length);
        }
        //DT传递的draw:
        String draw = request.getParameter("draw");
        if (StringUtils.isEmpty(draw)) {
            draw = Integer.toString(this.draw);
        }
        //DT传递
        this.setStart(Integer.parseInt(start));
        this.setLength(Integer.parseInt(length));
        this.setDraw(Integer.parseInt(draw));
        //计算页码
        this.pageNum = (Integer.parseInt(start) / Integer.parseInt(length)) + 1;

    }


    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public boolean isSearch() {
        return isSearch;
    }

    public void setSearch(boolean search) {
        isSearch = search;
    }

    public int[] getOrder() {
        return order;
    }

    public void setOrder(int[] order) {
        this.order = order;
    }

    public String getOrderDir() {
        return orderDir;
    }

    public void setOrderDir(String orderDir) {
        this.orderDir = orderDir;
    }

    public String getColumnsData() {
        return columnsData;
    }

    public void setColumnsData(String columnsData) {
        this.columnsData = columnsData;
    }

    public String getColumnsName() {
        return columnsName;
    }

    public void setColumnsName(String columnsName) {
        this.columnsName = columnsName;
    }

    public String getColumnsSearchable() {
        return columnsSearchable;
    }

    public void setColumnsSearchable(String columnsSearchable) {
        this.columnsSearchable = columnsSearchable;
    }

    public boolean isOrderable() {
        return isOrderable;
    }

    public void setOrderable(boolean orderable) {
        isOrderable = orderable;
    }

    public String getColumnsSearchValue() {
        return columnsSearchValue;
    }

    public void setColumnsSearchValue(String columnsSearchValue) {
        this.columnsSearchValue = columnsSearchValue;
    }

    public boolean isSearchRegex() {
        return isSearchRegex;
    }

    public void setSearchRegex(boolean searchRegex) {
        isSearchRegex = searchRegex;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDtRowId() {
        return dtRowId;
    }

    public void setDtRowId(String dtRowId) {
        this.dtRowId = dtRowId;
    }

    public String getDtRowClass() {
        return dtRowClass;
    }

    public void setDtRowClass(String dtRowClass) {
        this.dtRowClass = dtRowClass;
    }

    public Object getDtRowData() {
        return dtRowData;
    }

    public void setDtRowData(Object dtRowData) {
        this.dtRowData = dtRowData;
    }

    public Object getDtRowAttr() {
        return dtRowAttr;
    }

    public void setDtRowAttr(Object dtRowAttr) {
        this.dtRowAttr = dtRowAttr;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
