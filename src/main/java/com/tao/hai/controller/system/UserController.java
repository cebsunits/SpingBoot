package com.tao.hai.controller.system;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.tao.hai.annotation.Log;
import com.tao.hai.base.BaseController;
import com.tao.hai.base.DataTablePage;
import com.tao.hai.base.JsonModelBean;
import com.tao.hai.base.ParameterModelBean;
import com.tao.hai.bean.Dept;
import com.tao.hai.bean.Role;
import com.tao.hai.bean.User;
import com.tao.hai.json.AjaxError;
import com.tao.hai.json.AjaxJson;
import com.tao.hai.json.AjaxSuccess;
import com.tao.hai.service.DeptService;
import com.tao.hai.service.RoleService;
import com.tao.hai.service.UserService;
import com.tao.hai.util.JSONUtils;
import com.tao.hai.util.ParameterModelBeanUtil;
import com.tao.hai.util.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanght
 * @description 用户管理，表格采用datatables方式进行展示
 */
@Controller
@RequestMapping(value = "/sys/user")
public class UserController extends BaseController {
    private int hashIterations = 2;
    @Autowired
    UserService userService;
    @Autowired
    DeptService deptService;
    @Autowired
    RoleService roleService;

    @RequiresPermissions("sys:user:user")
    @GetMapping("")
    public String userList() {
        return "/user/userList";
    }

    @RequestMapping(value = "/get")
    public String get(User user) {
        User u = userService.getUser(user);
        return JSONObject.toJSONString(u);
    }

    @RequiresPermissions("sys:user:user")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public DataTablePage queryUserAll(HttpServletRequest request) {
        //组装成DataTables分页对象
        DataTablePage<User> dataTablePage = new DataTablePage<User>(request);
        //开始分页：PageHelper会处理接下来的第一个查询
        PageInfo<User> pageInfo = userService.queryByPage(dataTablePage.getPageNum(), dataTablePage.getPageSize(), null);
        //封装数据给DataTables
        dataTablePage.setData(pageInfo.getList());
        dataTablePage.setRecordsTotal((int) pageInfo.getTotal());
        dataTablePage.setRecordsFiltered(dataTablePage.getRecordsTotal());
        return dataTablePage;
    }

    @RequiresPermissions("sys:user:user")
    @RequestMapping("/list")
    @ResponseBody
    public Object list(User user,HttpServletRequest request) {
        /**Sort对象初始化错误，需要更换方法解决*/
        ParameterModelBean bean = ParameterModelBeanUtil.getParameterModelBean(user);
        bean = parmeterPage(request,bean);
        if(StringUtils.isEmpty(bean.getOrder())){
            bean.setOrder("userId" );
            bean.setSort("asc");
        }
        PageInfo<User> pageInfo = userService.getPageList(bean);
        return pageInfo;
    }

    /***跳转到添加页面*/
    @RequiresPermissions("sys:user:add")
    @Log(value = "添加用户")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(User user, Model model) {
        //获取角色信息
        List<Role> roles = roleService.findAll();
        /**只读属性*/
        model.addAttribute("readOnly", false);
        model.addAttribute("user", new User());
        model.addAttribute("roles", roles);
        return "/user/userAdd";
    }

    /***更改用户信息*/
    @RequiresPermissions("sys:user:edit")
    @Log(value = "编辑用户")
    @RequestMapping(value = "/edit")
    public String edit(String userId, Model model) {
        //获取角色信息
        List<Role> roles = roleService.userRoles(userId);
        //获取用户信息
        User user = userService.get(userId);
        /**只读属性*/
        model.addAttribute("readOnly", false);
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "/user/userAdd";
    }

    /***查看用户信息*/
    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = "/view")
    public String view(String userId, Model model) {
        //获取角色信息
        List<Role> roles = roleService.userRoles(userId);
        //获取用户信息
        User user = userService.get(userId);
        String userStr = JSONObject.toJSONString(user);
        /**只读属性*/
        model.addAttribute("readOnly", true);
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "/user/userAdd";
    }

    /**
     * 用户添加;
     *
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @Log(value = "保存用户")
    @ResponseBody
    public Object save(User user, HttpServletRequest request) {
        if (StringUtils.isEmpty(user.getUserId())) {
            Md5Hash md5Hash = new Md5Hash(user.getPassword(), user.getCredentialsSalt(), hashIterations);
            String encryptPwd = md5Hash.toString();
            user.setPassword(encryptPwd);
        } else {
            User oldUser = userService.get(user.getUserId());
            /**查找数据库中的用户信息，判断密码是否一致，一致则不更改密码，否则重新更改密码*/
            if (oldUser != null && !user.getPassword().equals(oldUser.getPassword())) {
                Md5Hash md5Hash = new Md5Hash(user.getPassword(), user.getCredentialsSalt(), hashIterations);
                String encryptPwd = md5Hash.toString();
                user.setPassword(encryptPwd);
            }
            user.setNewRecord(false);
        }
        try {
            //更新部门信息
            List<Dept> deptList = new ArrayList<>();
            String[] deptIds = user.getDeptIds();
            if (deptIds != null && deptIds.length > 0) {
                for (String deptId : deptIds) {
                    if (StringUtils.isNotEmpty(deptId)) {
                        Dept dept = deptService.getDept(deptId);
                        deptList.add(dept);
                    }
                }
            }
            user.setDeptList(deptList);
            //更新角色信息
            List<Role> roleList = new ArrayList<>();
            String[] roleIds = request.getParameterValues("role");
            if (roleIds != null && roleIds.length > 0) {
                for (String roleId : roleIds) {
                    if (StringUtils.isNotEmpty(roleId)) {
                        Role role = roleService.getRole(roleId);
                        roleList.add(role);
                    }
                }
            }
            user.setRoleList(roleList);
            //保存用户信息
            userService.save(user);
            AjaxSuccess ajaxJson = new AjaxSuccess();
            ajaxJson.setData(user);
            ajaxJson.setMessage("保存成功！");
            return ajaxJson;
        } catch (Exception e) {
            e.printStackTrace();
            AjaxError ajaxJson = new AjaxError();
            ajaxJson.setMessage("保存失败！");
            return ajaxJson;
        }
    }

    /**
     * 用户删除;
     *
     * @return
     */
    @RequiresPermissions("sys:user:remove")
    @Log(value = "删除用户")
    @RequestMapping("/delete")
    @ResponseBody
    public Object userDel(String userId) {
        AjaxJson ajaxJson;
        try {
            userService.delete(userId);
            ajaxJson = new AjaxSuccess();
            ajaxJson.setMessage("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxJson = new AjaxError();
            ajaxJson.setMessage("删除失败！");
        }
        return ajaxJson;
    }

    /**
     * 批量删除方法
     */
    @RequiresPermissions("sys:user:batchRemove")
    @Log(value = "批量删除用户")
    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    @ResponseBody
    public Object batchDelete(String userIdList, HttpServletResponse response) {
        AjaxJson ajaxJson;
        if (StringUtils.isEmpty(userIdList)) {
            ajaxJson = new AjaxError();
            ajaxJson.setMessage("用户信息为空，不能删除！");
        } else {
            try {
                String[] userIds = userIdList.split(",");
                //保存用户信息
                userService.delete(userIds);
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

    @RequestMapping("/checkUserExists")
    @ResponseBody
    public Object checkUserExists(String oldLoginName, String loginName) {
        AjaxJson ajaxJson;
        if (StringUtils.isNotEmpty(loginName)) {
            /**如果存在旧的登录名，并且旧的登录名与新登录名一样，则验证通过*/
            if (StringUtils.isNotEmpty(oldLoginName) && oldLoginName.equals(loginName)) {
                ajaxJson = new AjaxSuccess();
                ajaxJson.setMessage("用户名可用！");
            } else {
                /**验证新用户名是否已存在，存在则提示更换用户名*/
                boolean result = userService.checkUserExists(loginName);
                //已经存在用户
                if (result) {
                    ajaxJson = new AjaxError();
                    ajaxJson.setMessage("用户已存在，请更换用户名！");
                } else {
                    ajaxJson = new AjaxSuccess();
                    ajaxJson.setMessage("用户名可用！");
                }
            }
        } else {
            ajaxJson = new AjaxError();
            ajaxJson.setMessage("请输入用户名！");
        }
        return ajaxJson;
    }

    @RequiresPermissions("sys:user:resetPwd")
    @Log(value = "请求更改用户密码")
    @RequestMapping(value = "/toChangePassword")
    public String toChangePassword(Model model) {
        User user = ShiroUtils.getUser();
        model.addAttribute("user", user);
        return "/user/userPasswordReset";
    }

    @Log(value = "更改用户密码")
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    @ResponseBody
    public Object changePassword(String oldPassword, String newPassword, Model model) {
        User user = ShiroUtils.getUser();
        AjaxJson ajaxJson;
        if (user == null) {
            ajaxJson = new AjaxError();
            ajaxJson.setMessage("获取当前用户信息失败!");
            return ajaxJson;
        }
        /**验证密码是否正确*/
        Md5Hash md5Hash = new Md5Hash(oldPassword, user.getCredentialsSalt(), hashIterations);
        String encryptPwd = md5Hash.toString();
        if (!encryptPwd.equals(user.getPassword())) {
            ajaxJson = new AjaxError();
            ajaxJson.setMessage("当前用户密码不正确!");
            return ajaxJson;
        }
        /**更改密码*/
        Md5Hash md5HashNew = new Md5Hash(newPassword, user.getCredentialsSalt(), hashIterations);
        String encryptNewPwd = md5HashNew.toString();
        user.setPassword(encryptNewPwd);
        userService.updatePassword(user);
        ajaxJson = new AjaxSuccess();
        ajaxJson.setMessage("密码修改成功，请重新登录");
        return ajaxJson;
    }

    /***bootstrap validator remote 验证方法*/
    @RequestMapping(value = "/toCheckPwd")
    @ResponseBody
    public Object checkCurrentPwd(@RequestParam String password) {
        AjaxJson ajaxJson;
        if (StringUtils.isEmpty(password)) {
            ajaxJson = new AjaxError();
            ajaxJson.setMessage("密码不能为空！");
            return ajaxJson;
        }
        User user = ShiroUtils.getUser();
        if (user == null) {
            ajaxJson = new AjaxError();
            ajaxJson.setMessage("获取当前登录用户信息失败！");
            return ajaxJson;
        }
        Md5Hash md5HashNew = new Md5Hash(password, user.getCredentialsSalt(), hashIterations);
        String encryptPwd = md5HashNew.toString();
        if (!encryptPwd.equals(user.getPassword())) {
            ajaxJson = new AjaxError();
            ajaxJson.setMessage("密码错误！");
            return ajaxJson;
        }
        ajaxJson = new AjaxSuccess();
        ajaxJson.setMessage("验证密码成功！");
        return ajaxJson;
    }

}
