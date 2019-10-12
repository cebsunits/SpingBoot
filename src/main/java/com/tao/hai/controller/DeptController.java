package com.tao.hai.controller;

import com.tao.hai.base.BootstrapUITreeNode;
import com.tao.hai.bean.Dept;
import com.tao.hai.service.DeptService;
import com.tao.hai.json.AjaxError;
import com.tao.hai.json.AjaxJson;
import com.tao.hai.json.AjaxSuccess;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/dept")
public class DeptController {
    @Autowired
    DeptService deptService;

    @RequestMapping(value="/index")
    public String index(){

        return "/dept/deptList";
    }
    @RequestMapping(value="/add")
    @RequiresPermissions("dept:add")
    public String toAdd(){
        return "/dept/deptAdd";
    }
    @RequestMapping(value="/save")
    @RequiresPermissions("dept:add")
    public String save(Dept dept){
        deptService.save(dept);
        return "/dept/deptAdd";
    }
    @RequestMapping(value="/view")
    public String view(String deptId){
       Dept dept= deptService.getDept(deptId);
        return "";
    }
    @RequestMapping(value="/update")
    public String update(){

        return "";
    }
    @RequestMapping(value="deptTree")
    @ResponseBody
    public Object deptTree(Dept dept,HttpServletRequest request, Model model){
        List<BootstrapUITreeNode> list=deptService.buildDeptTree(dept);
        AjaxJson ajaxJson;
        if(list!=null&&list.size()>0){
            ajaxJson=new AjaxSuccess();
            ajaxJson.setData( list);
        }else{
            ajaxJson=new AjaxError();
            ajaxJson.setMessage("获取部门树失败！");
        }
        return ajaxJson;
    }
}
