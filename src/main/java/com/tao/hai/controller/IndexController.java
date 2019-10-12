package com.tao.hai.controller;

import com.github.pagehelper.util.StringUtil;
import com.tao.hai.bean.Menu;
import com.tao.hai.bean.User;
import com.tao.hai.service.MenuService;
import com.tao.hai.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value="/")
public class IndexController {
    @Autowired
    UserService userService;
    @Autowired
    MenuService menuService;
    public String index(Model model){
        String loginName = (String) SecurityUtils.getSubject().getPrincipal();
        if (StringUtil.isEmpty(loginName)) {
            return "login";
        }
        User user=userService.getUser(loginName);
        List<Menu> menuList=menuService.getList(loginName);
        model.addAttribute("user", user);
        model.addAttribute("menuList", menuList);
        return "index";
    }
}
