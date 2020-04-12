package com.tao.hai.controller.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/bootstrapTableDemo")
public class BootStrapTreeTableController {

    public String index(){
        return "/pluggins/bootstrapTreeTable/demo";
    }
}
