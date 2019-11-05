package com.tao.hai.controller;

import com.tao.hai.base.BootstrapUITreeNode;
import com.tao.hai.bean.Dept;
import com.tao.hai.facotry.LogFactory;
import com.tao.hai.json.AjaxError;
import com.tao.hai.json.AjaxJson;
import com.tao.hai.json.AjaxSuccess;
import com.tao.hai.service.DeptService;
import com.tao.hai.service.LogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/dept")
public class DeptController {
    @Autowired
    DeptService deptService;

    @Autowired
    LogService logService;

    @RequestMapping(value = "/index")
    public String index() {

        return "/dept/deptList";
    }
    @RequestMapping(value = "/deptTreeTable")
    public String deptTreeTable() {
        return "/dept/deptTreeTableList";
    }

    @ResponseBody
    @RequestMapping(value = "/list")
    public List<Dept> list(Dept dept) {
        List<Dept> deptList = deptService.findList(dept);
        return deptList;
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAdd(String deptId, Model model) {
        Dept dept = new Dept();
        if (StringUtils.isNotEmpty(deptId)) {
            dept.setParentId(deptId);
        }
        model.addAttribute("dept", dept);
        return "/dept/deptAdd";
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    public AjaxJson save(Dept dept, HttpServletRequest request) {
        AjaxJson ajaxJson;
        try {
            deptService.save(dept);
            logService.save(LogFactory.createSysLog("新增或修改部门", "部门：" + dept.getDeptName()));
            ajaxJson = new AjaxSuccess();
            ajaxJson.setMessage("保存成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxJson = new AjaxError();
            ajaxJson.setMessage("失败成功！" + e.getMessage());
        }
        return ajaxJson;
    }


    @RequestMapping(value = "/view")
    public String view(String deptId, Model model) {
        Dept dept = new Dept();
        if (StringUtils.isNotEmpty(deptId)) {
            dept = deptService.getDept(deptId);
        }
        model.addAttribute("dept", dept);
        /**只读属性*/
        model.addAttribute("readOnly", true);
        return "/dept/deptAdd";
    }

    @RequestMapping(value = "/update")
    public String update(String deptId, Model model) {
        Dept dept = new Dept();
        if (StringUtils.isNotEmpty(deptId)) {
            dept = deptService.getDept(deptId);
        }
        model.addAttribute("dept", dept);
        /**只读属性*/
        model.addAttribute("readOnly", false);
        return "/dept/deptAdd";
    }

    @RequestMapping("/checkUserExists")
    @ResponseBody
    public Object checkUserExists(String deptName) {
        AjaxJson ajaxJson;
        if (StringUtils.isNotEmpty(deptName)) {
            boolean result = deptService.checkDeptExists(deptName);
            //已经存在用户
            if (result) {
                ajaxJson=new AjaxError();
                ajaxJson.setMessage("部门已存在，请重新输入！");
            } else {
                ajaxJson=new AjaxSuccess();
                ajaxJson.setMessage("部门名称可用！");
            }
        } else {
            ajaxJson=new AjaxError();
            ajaxJson.setMessage("请输入部门名称！");
        }
        return ajaxJson;
    }


    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(String deptId) {
        AjaxJson ajaxJson;
        if (StringUtils.isNotEmpty(deptId)) {
            deptService.delete(deptId);
            ajaxJson = new AjaxSuccess();
            ajaxJson.setMessage("删除成功！");
        } else {
            ajaxJson = new AjaxError();
            ajaxJson.setMessage("删除失败！");
        }
        return ajaxJson;
    }

    @RequestMapping(value = "/batchDelete")
    @ResponseBody
    public Object batchDelete(String[] deptIds) {
        AjaxJson ajaxJson;
        if (deptIds != null && deptIds.length > 0) {
            deptService.delete(deptIds);
            ajaxJson = new AjaxSuccess();
            ajaxJson.setMessage("删除成功！");
        } else {
            ajaxJson = new AjaxError();
            ajaxJson.setMessage("删除失败！");
        }
        return ajaxJson;
    }

    @RequestMapping(value = "/deptTreeView")
    public String deptTreeView(HttpServletRequest request) {

        return "/dept/deptTreeView";
    }

    @RequestMapping(value = "/deptTree")
    @ResponseBody
    public Object deptTree(Dept dept, HttpServletRequest request, Model model) {
        List<BootstrapUITreeNode> list = deptService.buildDeptTree(dept);
        AjaxJson ajaxJson;
        if (list != null && list.size() > 0) {
            ajaxJson = new AjaxSuccess();
            ajaxJson.setData(list);
        } else {
            ajaxJson = new AjaxError();
            ajaxJson.setMessage("获取部门树失败！");
        }
        return ajaxJson;
    }
}
