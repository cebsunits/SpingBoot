package com.tao.hai.controller;

import com.github.pagehelper.PageInfo;
import com.tao.hai.base.DataTablePage;
import com.tao.hai.bean.Menu;
import com.tao.hai.json.AjaxError;
import com.tao.hai.json.AjaxJson;
import com.tao.hai.json.AjaxSuccess;
import com.tao.hai.service.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value="/sys/menu")
public class MenuController {
    @Autowired
    MenuService menuService;
    @RequestMapping(value="index")
    public String index(){
        return "/menu/menuList";
    }
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public DataTablePage queryUserAll(HttpServletRequest request) {
        //组装成DataTables分页对象
        DataTablePage<Menu> dataTablePage = new DataTablePage<Menu>(request);
        //开始分页：PageHelper会处理接下来的第一个查询
        PageInfo<Menu> pageInfo = menuService.queryByPage(dataTablePage.getPageNum(), dataTablePage.getPageSize(), null);
        //封装数据给DataTables
        dataTablePage.setData(pageInfo.getList());
        dataTablePage.setRecordsTotal((int) pageInfo.getTotal());
        dataTablePage.setRecordsFiltered(dataTablePage.getRecordsTotal());
        return dataTablePage;
    }

    @RequestMapping(value="form")
    public String form(Menu menu, Model model,HttpServletRequest request){
        /**只读属性*/
        String readOnly=request.getParameter("readOnly");
        if("true".equals(readOnly)){
        } else {
            readOnly = "false";
        }
        //获取父节点信息
        Menu parent=new Menu();
        parent.setMenuId(menu.getParentId());
        Menu mParent=null;
        if(StringUtils.isEmpty(menu.getParentId())){
            mParent=new Menu();
        }else{
            mParent=menuService.get(parent);
        }
        menu.setParent(mParent);
        /**只读属性*/
        model.addAttribute("readOnly", readOnly);
        model.addAttribute("menu",menu);
        return "/menu/menuForm";
    }
    /**保存方法*/
    @RequestMapping(value="save")
    @ResponseBody
    public Object save(Menu menu,Model model){
        AjaxJson ajaxJson;
        try {
            menuService.save(menu);
            ajaxJson=new AjaxSuccess();
            ajaxJson.setMessage("保存成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxJson=new AjaxError();
            ajaxJson.setMessage("保存失败！");
        }
        return ajaxJson;
    }
    /**删除方法*/
    @RequestMapping(value="delete")
    public Object delete(Menu menu,Model model){
        AjaxJson ajaxJson;
        try {
            menuService.delelte(menu);
            ajaxJson=new AjaxSuccess();
            ajaxJson.setMessage("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxJson=new AjaxError();
            ajaxJson.setMessage("删除失败！");
        }
        return ajaxJson;
    }
    /**批量删除方法*/
    @RequestMapping(value="batchDelete")
    public Object batchDelete(String[] ids,Model model){
        AjaxJson ajaxJson;
        try {
            menuService.delete(ids);
            ajaxJson=new AjaxSuccess();
            ajaxJson.setMessage("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxJson=new AjaxError();
            ajaxJson.setMessage("删除失败！");
        }
        return ajaxJson;
    }


}
