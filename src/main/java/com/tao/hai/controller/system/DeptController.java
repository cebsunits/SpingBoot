package com.tao.hai.controller.system;

import com.tao.hai.annotation.Log;
import com.tao.hai.base.ParameterModelBean;
import com.tao.hai.base.TreeNode;
import com.tao.hai.bean.Dept;
import com.tao.hai.facotry.LogFactory;
import com.tao.hai.json.AjaxError;
import com.tao.hai.json.AjaxJson;
import com.tao.hai.json.AjaxSuccess;
import com.tao.hai.service.DeptService;
import com.tao.hai.service.LogService;
import com.tao.hai.util.ParameterModelBeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/sys/dept")
public class DeptController {
    @Autowired
    DeptService deptService;

    @Autowired
    LogService logService;

    @RequiresPermissions("sys:dept:dept")
    @GetMapping("")
    public String index() {

        return "/dept/deptList";
    }

    @RequiresPermissions("sys:dept:dept")
    @RequestMapping(value = "/deptTreeTable")
    public String deptTreeTable() {
        return "/dept/deptTreeTableList";
    }

    @RequiresPermissions("sys:dept:dept")
    @RequestMapping(value = "/list")
    @ResponseBody
    public List<Dept> list(Dept dept) {
        /**Sort对象初始化错误，需要更换方法解决*/
        ParameterModelBean bean = ParameterModelBeanUtil.getParameterModelBean(dept);
        /**排序方式*/
        bean.setOrder("deptSort");
        bean.setSort("asc");
        List<Dept> deptList = deptService.getList(bean);
        return deptList;
    }

    @RequiresPermissions(value = {"sys:dept:add", "sys:dept:edit"})
    @Log("新增菜单")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAdd(Dept dept, Model model, HttpServletRequest request) {
        /**只读属性*/
        String readOnly = request.getParameter("readOnly");
        if ("true".equals(readOnly)) {
        } else {
            readOnly = "false";
        }
        /**menuId不为空时获取menu菜单信息*/
        if (StringUtils.isNotEmpty(dept.getDeptId())) {
            dept = deptService.get(dept);
        }
        if (dept == null) {
            dept = new Dept();
        }
        //获取父节点信息
        Dept parent = new Dept();
        parent.setDeptId(dept.getParentId());
        if (StringUtils.isNotEmpty(parent.getParentId())) {
            parent = deptService.get(parent);
        }
        dept.setParent(parent);

        model.addAttribute("dept", dept);
        /**只读属性*/
        model.addAttribute("readOnly", readOnly);
        return "/dept/deptAdd";
    }

    @Log("保持菜单")
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

    @RequestMapping("/checkExists")
    @ResponseBody
    public Object checkExists(String deptCode, String oldDeptCode) {
        AjaxJson ajaxJson;
        if (StringUtils.isNotEmpty(deptCode) ) {
            /**默认false*/
            boolean result = false;
            /**说明是更新操作，可直接通过*/
            if (!deptCode.equals(oldDeptCode)) {
                result = deptService.checkDeptExists(deptCode);
            }
            //已经存在用户
            if (result) {
                ajaxJson = new AjaxError();
                ajaxJson.setMessage("部门编码已存在，请重新输入！");
            } else {
                ajaxJson = new AjaxSuccess();
                ajaxJson.setMessage("部门编码可用！");
            }
        } else {
            ajaxJson = new AjaxError();
            ajaxJson.setMessage("请输入部门编码！");
        }
        return ajaxJson;
    }

    @RequiresPermissions("sys:dept:remove")
    @Log("删除菜单")
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

    @Log("批量删除菜单")
    @RequiresPermissions("sys:dept:batchRemove")
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
        List<TreeNode<Dept>> list = deptService.buildDeptTree(dept);
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
