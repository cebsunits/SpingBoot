package com.tao.hai.controller;

import com.github.pagehelper.PageInfo;
import com.tao.hai.base.DataTablePage;
import com.tao.hai.bean.Role;
import com.tao.hai.facotry.LogFactory;
import com.tao.hai.json.AjaxError;
import com.tao.hai.json.AjaxJson;
import com.tao.hai.json.AjaxSuccess;
import com.tao.hai.service.LogService;
import com.tao.hai.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/role")
public class RoleController {
    /**
     * @author zhanght
     * @description 角色管理，表格采用 bootstrap-table 方式进行展示
     */
    @Autowired
    RoleService roleService;
    @Autowired
    LogService logService;

    @RequestMapping("/index")
    public String index() {
        return "/role/roleList";
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public DataTablePage queryUserAll(HttpServletRequest request) {
        //组装成DataTables分页对象
        DataTablePage<Role> dataTablePage = new DataTablePage<Role>(request);
        //开始分页：PageHelper会处理接下来的第一个查询
        PageInfo<Role> pageInfo = roleService.queryByPage(dataTablePage.getPageNum(), dataTablePage.getPageSize(), null);
        //封装数据给DataTables
        dataTablePage.setData(pageInfo.getList());
        dataTablePage.setRecordsTotal((int) pageInfo.getTotal());
        dataTablePage.setRecordsFiltered(dataTablePage.getRecordsTotal());
        return dataTablePage;
    }

    @RequestMapping("/roleList")
    public Object roleList(HttpServletRequest request, HttpServletResponse response) {
        int pageSize = 10;
        try {
            pageSize = Integer.parseInt(request.getParameter("pageSize"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        int pageNumber = 0;
        try {
            pageNumber = Integer.parseInt(request.getParameter("pageNumber")) - 1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        String strRole = request.getParameter("searchText") == null ? "" : request.getParameter("searchText");

        String sortName = request.getParameter("sortName") == null ? "roleId" : request.getParameter("sortName");
        String sortOrder = request.getParameter("sortOrder") == null ? "asc" : request.getParameter("sortOrder");
        /**Sort对象初始化错误，需要更换方法解决*/
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,sortName);
        List<Role> roleList = roleService.findAll();
        return roleList;
    }

    @RequestMapping("/checkRoleExists")
    @ResponseBody
    public Object checkRoleExists(String role) {
        AjaxJson ajaxJson ;
        if (StringUtils.isNotEmpty(role)) {
            boolean result = roleService.checkRoleExists(role);
            //已经存在用户
            if (result) {
                ajaxJson=new AjaxError();
                ajaxJson.setMessage("角色已存在！");
            } else {
                ajaxJson=new AjaxSuccess();
                ajaxJson.setMessage("角色可用！");
            }
        } else {
            ajaxJson=new AjaxError();
            ajaxJson.setMessage("请输入角色！");
        }
        return ajaxJson;
    }


    @RequestMapping(value = "/roleAdd", method = RequestMethod.GET)
    public String toAdd(Role role) {
        return "/role/roleAdd";
    }

    @RequestMapping(value = "/roleAdd", method = RequestMethod.POST)
    @ResponseBody
    public Object save(Role role) {
        if (StringUtils.isEmpty(role.getRoleId())) {
            role.setCreateDate(new Date());
        } else {
            role.setNewRecord(false);
        }
        try {
            role.setUpdateDate(new Date());
            //保存用户信息
            roleService.save(role);
            logService.save(LogFactory.createSysLog("新增或修改用户角色", "角色：" + role.getRole()));
            AjaxSuccess ajaxJson = new AjaxSuccess();
            ajaxJson.setData(role);
            return ajaxJson;
        } catch (Exception e) {
            e.printStackTrace();
            AjaxError ajaxJson = new AjaxError();
            ajaxJson.setMessage("保存失败！");
            return ajaxJson;
        }
    }
}
