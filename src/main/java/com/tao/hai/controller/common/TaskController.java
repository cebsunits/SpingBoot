package com.tao.hai.controller.common;

import com.tao.hai.json.AjaxJson;
import com.tao.hai.json.AjaxSuccess;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.web.bind.annotation.GetMapping;

public class TaskController {


    /**启动定时任务*/
    @GetMapping(value="startTask")
    @ApiOperation(value = "启动定时任务",notes = "启动定时任务",httpMethod = "RequestMethod.GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "",value = "",required = true,dataType = "")})
    public AjaxJson startTask(){
        AjaxJson ajaxJson;

        ajaxJson=new AjaxSuccess();
        ajaxJson.setMessage("启动成功");
        return ajaxJson;
    }

}
