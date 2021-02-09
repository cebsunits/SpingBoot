package com.tao.hai.controller.system;

import com.github.pagehelper.PageInfo;
import com.tao.hai.annotation.Log;
import com.tao.hai.base.BaseController;
import com.tao.hai.base.DataTablePage;
import com.tao.hai.base.ParameterModelBean;
import com.tao.hai.bean.Menu;
import com.tao.hai.bean.Role;
import com.tao.hai.bean.User;
import com.tao.hai.json.AjaxError;
import com.tao.hai.json.AjaxJson;
import com.tao.hai.json.AjaxSuccess;
import com.tao.hai.service.MenuService;
import com.tao.hai.service.RoleService;
import com.tao.hai.util.JSONUtils;
import com.tao.hai.util.ParameterModelBeanUtil;
import com.tao.hai.util.ShiroUtils;
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
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/sys/role")
public class RoleController extends BaseController {
    /**
     * @author zhanght
     * @description 角色管理，表格采用 bootstrap-table 方式进行展示
     */
    @Autowired
    RoleService roleService;
    @Autowired
    MenuService menuService;


    @RequiresPermissions("sys:role:role")
    @GetMapping("")
    public String index() {
        return "/role/roleList";
    }

    @RequiresPermissions("sys:role:role")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public DataTablePage queryUserAll(HttpServletRequest request) {
        //组装成DataTables分页对象
        DataTablePage<Role> dataTablePage = new DataTablePage<Role>(request);
        //开始分页：PageHelper会处理接下来的第一个查询
        PageInfo<Role> pageInfo = roleService.queryByPage(dataTablePage.getPageNum(), dataTablePage.getPageSize(), new Role());
        //封装数据给DataTables
        dataTablePage.setData(pageInfo.getList());
        dataTablePage.setRecordsTotal((int) pageInfo.getTotal());
        dataTablePage.setRecordsFiltered(dataTablePage.getRecordsTotal());
        return dataTablePage;
    }

    @RequiresPermissions("sys:role:role")
    @RequestMapping("/list")
    @ResponseBody
    public Object list(Role role,HttpServletRequest request) {
        ParameterModelBean bean = ParameterModelBeanUtil.getParameterModelBean(role);
        bean = parmeterPage(request,bean);
        if(StringUtils.isEmpty(bean.getOrder())){
            bean.setOrder("roleId" );
            bean.setSort("asc");
        }
        PageInfo<Role> pageInfo = roleService.getPageList(bean);
        return pageInfo;
    }

    @RequestMapping("/checkRoleExists")
    @ResponseBody
    public Object checkRoleExists(String role, String oldRole) {
        AjaxJson ajaxJson;
        if (StringUtils.isNotEmpty(role)) {
            boolean result = false;
            if (!role.equals(oldRole)) {
                result = roleService.checkRoleExists(role);
            }
            //已经存在用户
            if (result) {
                ajaxJson = new AjaxError();
                ajaxJson.setMessage("角色已存在！");
            } else {
                ajaxJson = new AjaxSuccess();
                ajaxJson.setMessage("角色可用！");
            }
        } else {
            ajaxJson = new AjaxError();
            ajaxJson.setMessage("请输入角色！");
        }
        return ajaxJson;
    }

    @RequiresPermissions("sys:role:add")
    @Log(value = "添加角色")
    @RequestMapping(value = "/roleAdd", method = RequestMethod.GET)
    public String toAdd(Role role, Model model) {
        if (StringUtils.isNotEmpty(role.getRoleId())) {
            role = roleService.getRole(role.getRoleId());
        }
        model.addAttribute("role", JSONUtils.toJsonObject(role));
        return "/role/roleAdd";
    }

    @Log(value = "保存角色")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Object save(Role role, HttpServletResponse response) {
        AjaxJson ajaxJson;
        try {
            //保存用户信息
            roleService.save(role);
            ajaxJson = new AjaxSuccess();
            ajaxJson.setData(role);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxJson = new AjaxError();
            ajaxJson.setMessage("保存失败！");
        }
        return ajaxJson;
    }

    @RequiresPermissions("sys:role:remove")
    @Log(value = "删除角色")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Object delete(Role role, HttpServletResponse response) {
        AjaxJson ajaxJson;
        if (StringUtils.isEmpty(role.getRoleId())) {
            ajaxJson = new AjaxError();
            ajaxJson.setMessage("角色信息为空，不能删除！");
        } else {
            try {
                //保存用户信息
                roleService.del(role);
                ajaxJson = new AjaxSuccess();
                ajaxJson.setMessage("删除成功！");
                ajaxJson.setData(role);
            } catch (Exception e) {
                e.printStackTrace();
                ajaxJson = new AjaxError();
                ajaxJson.setMessage("删除失败！");
            }
        }
        return ajaxJson;
    }

    /**
     * 批量删除方法
     */
    @RequiresPermissions("sys:role:batchRemove")
    @Log(value = "批量删除角色")
    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    @ResponseBody
    public Object batchDelete(String roleIdList, HttpServletResponse response) {
        AjaxJson ajaxJson;
        if (StringUtils.isEmpty(roleIdList)) {
            ajaxJson = new AjaxError();
            ajaxJson.setMessage("角色信息为空，不能删除！");
        } else {
            try {
                String[] roleIds = roleIdList.split(",");
                //保存用户信息
                roleService.delete(roleIds);
                ajaxJson = new AjaxSuccess();
                ajaxJson.setMessage("删除成功！");
            } catch (Exception e) {
                e.printStackTrace();
                ajaxJson = new AjaxError();
                ajaxJson.setMessage("删除失败！");
            }
        }
        return ajaxJson;
    }

    /**
     * 授权功能
     */
    @RequiresPermissions("sys:role:grant")
    @Log(value = "角色授权")
    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public String auth(Role role, Model model) {
        AjaxJson ajaxJson;
        if (StringUtils.isEmpty(role.getRoleId())) {
            ajaxJson = new AjaxError();
            ajaxJson.setMessage("角色信息为空！");
        }
        role = roleService.getRole(role.getRoleId());
        /**获取角色选择的列表信息*/
        List<Menu> roleMenu = menuService.getRoleMenus(role.getRoleId());
        /**前台js中使用，需要转换为JSON对象*/
        model.addAttribute("role", role);
        /**放置角色菜单信息*/
        model.addAttribute("roleMenuList", roleMenu);
        /**放置所有菜单信息*/
        model.addAttribute("menuList", menuService.findAll());
        return "/role/roleAuth";
    }

    /**
     * 保存授权信息成功
     */
    @Log(value = "保存角色授权")
    @RequestMapping(value = "/saveAuth", method = RequestMethod.POST)
    @ResponseBody
    public Object saveAuth(Role role, Model model) {
        User user = ShiroUtils.getUser();
        AjaxJson ajaxJson;
        if (user == null || StringUtils.isEmpty(user.getUserId())) {
            ajaxJson = new AjaxError();
            ajaxJson.setMessage("获取当前用户信息失败！");
        }
        if (role == null || StringUtils.isEmpty(role.getRoleId())) {
            ajaxJson = new AjaxError();
            ajaxJson.setMessage("角色信息为空，不允许保存！");
            return ajaxJson;
        }
        if (role.getMenuIds() == null || role.getMenuIds().length == 0) {
            ajaxJson = new AjaxError();
            ajaxJson.setMessage("菜单信息为空，不允许保存！");
            return ajaxJson;
        }
        /**看是否能够查询到数据*/
        List<Menu> menuList = new ArrayList<Menu>();
        for (String menuId : role.getMenuIds()) {
            Menu queryMenu = new Menu();
            queryMenu.setMenuId(menuId);
            Menu menu = menuService.getMenu(queryMenu);
            if (menu != null) {
                menuList.add(menu);
            }
        }
        role.setMenuList(menuList);
        roleService.grant(role);
        model.addAttribute("role", role);
        model.addAttribute("menuList", role.getMenuList());
        ajaxJson = new AjaxSuccess();
        ajaxJson.setMessage("角色授权成功！");
        return ajaxJson;
    }
}
