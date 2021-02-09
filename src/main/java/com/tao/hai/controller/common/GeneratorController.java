package com.tao.hai.controller.common;

import com.alibaba.fastjson.JSON;
import com.tao.hai.bean.common.Table;
import com.tao.hai.json.AjaxError;
import com.tao.hai.json.AjaxJson;
import com.tao.hai.json.AjaxSuccess;
import com.tao.hai.service.GeneratorService;
import com.tao.hai.util.DateUtils;
import com.tao.hai.util.GeneratorUtil;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**代码生成功能*/
@Controller
@RequestMapping(value="/common/generator")
public class GeneratorController {
    /**前缀*/
    private String prefix="common/generator";

    @Autowired
    GeneratorService generatorService;
    @GetMapping()
    public String generator(){
        return prefix+"/list";
    }
    /**查询表*/
    @ResponseBody
    @GetMapping("/list")
    List<Table> list() {
        List<Table> list = generatorService.queryTables();
        return list;
    }
    /***更新生成配置文件*/
    @GetMapping("/edit")
    public String edit(Model model) {
        PropertiesConfiguration conf = GeneratorUtil.getConfig();
        Map<String, Object> property = new HashMap<String, Object>(16);
        property.put("author", conf.getProperty("author"));
        property.put("email", conf.getProperty("email"));
        property.put("package", conf.getProperty("package"));
        property.put("autoRemovePre", conf.getProperty("autoRemovePre"));
        property.put("tablePrefix", conf.getProperty("tablePrefix"));
        model.addAttribute("property", property);
        return prefix + "/edit";
    }
    /***更新生成配置文件*/
    @ResponseBody
    @PostMapping("/update")
    public Object update(@RequestParam Map<String, Object> map) {
        AjaxJson ajaxJson;
        try {
            PropertiesConfiguration conf = GeneratorUtil.getConfig();
            conf.setProperty("author", map.get("author"));
            conf.setProperty("email", map.get("email"));
            conf.setProperty("package", map.get("package"));
            conf.setProperty("autoRemovePre", map.get("autoRemovePre"));
            conf.setProperty("tablePrefix", map.get("tablePrefix"));
            conf.save();
            ajaxJson=new AjaxSuccess();
            ajaxJson.setMessage("保存成功");
        } catch (ConfigurationException e) {
            ajaxJson=new AjaxError();
            ajaxJson.setMessage("保存配置文件出错");
            return ajaxJson;
        }
        return ajaxJson;
    }
    /**生成代码**/
    @RequestMapping("/code/{tableName}")
    public void code(HttpServletRequest request, HttpServletResponse response,
                     @PathVariable("tableName") String tableName) throws IOException {
        String[] tableNames = new String[] { tableName };
        byte[] data = generatorService.generatorCode(tableNames);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\""+ DateUtils.getDate() +".zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }
    /**批量生成代码*/
    @RequestMapping("/batchCode")
    public void batchCode(HttpServletRequest request, HttpServletResponse response, String tables) throws IOException {
        String[] tableNames = new String[] {};
        tableNames = JSON.parseArray(tables).toArray(tableNames);
        byte[] data = generatorService.generatorCode(tableNames);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\""+ DateUtils.getDate() +".zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }
}
