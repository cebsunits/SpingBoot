package com.tao.hai.controller.system;

import com.github.pagehelper.PageInfo;
import com.tao.hai.annotation.Log;
import com.tao.hai.base.BaseController;
import com.tao.hai.base.DataTablePage;
import com.tao.hai.base.ParameterModelBean;
import com.tao.hai.bean.Menu;
import com.tao.hai.json.AjaxError;
import com.tao.hai.json.AjaxJson;
import com.tao.hai.json.AjaxSuccess;
import com.tao.hai.service.MenuService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/sys/menu")
public class MenuController extends BaseController {
    @Autowired
    MenuService menuService;

    @RequiresPermissions("sys:menu:menu")
    @GetMapping("")
    public String index() {
        return "/menu/menuList";
    }

    @RequiresPermissions("sys:menu:menu")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public DataTablePage queryUserAll(HttpServletRequest request) {
        //组装成DataTables分页对象
        DataTablePage<Menu> dataTablePage = new DataTablePage<Menu>(request);
        //开始分页：PageHelper会处理接下来的第一个查询
        PageInfo<Menu> pageInfo = menuService.queryByPage(dataTablePage.getPageNum(), dataTablePage.getPageSize(), new Menu());
        //封装数据给DataTables
        dataTablePage.setData(pageInfo.getList());
        dataTablePage.setRecordsTotal((int) pageInfo.getTotal());
        dataTablePage.setRecordsFiltered(dataTablePage.getRecordsTotal());
        return dataTablePage;
    }

    @RequiresPermissions("sys:menu:menu")
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {

        String sortName = "menuSort";
        if (params.containsKey("sort")) {
            Object sort = params.get("sort");
            if (ObjectUtils.isNotEmpty(sort)) {
                sortName = sort.toString();
            }
        }
        String sortOrder = "asc";
        if (params.containsKey("sortOrder")) {
            Object sort = params.get("sortOrder");
            if (ObjectUtils.isNotEmpty(sort)) {
                sortOrder = sort.toString();
            }
        }
        /**Sort对象初始化错误，需要更换方法解决*/
        ParameterModelBean bean = new ParameterModelBean();
        bean.setOrder("asc");
        bean.setSort(sortName);
        List<Menu> list = menuService.getList(bean);
        return list;
    }

    @RequiresPermissions(value = {"sys:menu:add", "sys:menu:edit"})
    @Log("新增/更新菜单")
    @RequestMapping(value = "form", method = RequestMethod.GET)
    public String form(Menu menu, Model model, HttpServletRequest request) {
        /**只读属性*/
        String readOnly = request.getParameter("readOnly");
        if ("true".equals(readOnly)) {
        } else {
            readOnly = "false";
        }
        /**menuId不为空时获取menu菜单信息*/
        if (StringUtils.isNotEmpty(menu.getMenuId())) {
            menu = menuService.get(menu);
        }
        if (menu == null) {
            menu = new Menu();
        }
        //获取父节点信息
        Menu parent = new Menu();
        parent.setMenuId(menu.getParentId());
        Menu mParent = null;
        if (StringUtils.isEmpty(menu.getParentId())) {
            mParent = new Menu();
        } else {
            mParent = menuService.get(parent);
        }
        menu.setParent(mParent);
        /**只读属性*/
        model.addAttribute("readOnly", readOnly);
        model.addAttribute("menu", menu);
        return "/menu/menuForm";
    }

    /**
     * 保存方法
     */
    @RequestMapping(value = "save")
    @Log("保存菜单")
    @ResponseBody
    public Object save(Menu menu, Model model) {
        AjaxJson ajaxJson;
        try {
            Menu exits = menuService.getByKey(menu);
            if (exits != null) {
                menu.setNewRecord(false);
            }
            menuService.save(menu);
            ajaxJson = new AjaxSuccess();
            ajaxJson.setMessage("保存成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxJson = new AjaxError();
            ajaxJson.setMessage("保存失败！");
        }
        return ajaxJson;
    }

    /**
     * 删除方法
     */
    @RequiresPermissions("sys:menu:remove")
    @Log("删除菜单")
    @RequestMapping(value = "delete")
    @ResponseBody
    public Object delete(Menu menu, Model model) {
        AjaxJson ajaxJson;
        try {
            menuService.del(menu);
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
    @RequiresPermissions("sys:menu:batchRemove")
    @Log("批量删除菜单")
    @RequestMapping(value = "batchDelete")
    @ResponseBody
    public Object batchDelete(String[] ids, Model model) {
        AjaxJson ajaxJson;
        try {
            menuService.delete(ids);
            ajaxJson = new AjaxSuccess();
            ajaxJson.setMessage("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxJson = new AjaxError();
            ajaxJson.setMessage("删除失败！");
        }
        return ajaxJson;
    }

}
