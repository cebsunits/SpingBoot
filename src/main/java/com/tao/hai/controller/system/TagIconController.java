package com.tao.hai.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/tagIcon")
public class TagIconController {
    @RequestMapping(value = "/iconSelect")
    public String iconSelect(HttpServletRequest request, Model model) {
        String value = request.getParameter("value");
        model.addAttribute("value", value);
        return "/common/tagIcon";
    }

}
