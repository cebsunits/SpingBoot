package com.tao.hai.quartz.controller;


import com.github.pagehelper.PageInfo;
import com.tao.hai.base.BaseController;
import com.tao.hai.base.DataTablePage;
import com.tao.hai.base.ParameterModelBean;
import com.tao.hai.bean.User;
import com.tao.hai.json.AjaxError;
import com.tao.hai.json.AjaxJson;
import com.tao.hai.json.AjaxSuccess;
import com.tao.hai.quartz.bean.QuartzJob;
import com.tao.hai.quartz.cron.CronExpression;
import com.tao.hai.quartz.service.QuartzJobService;
import com.tao.hai.util.DateUtils;
import com.tao.hai.util.ParameterModelBeanUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 
 * 
 * @author sunits
 * @email 112433877@qq.com
 * @date 2020-06-28 16:06:37
 */
 
@Controller
@RequestMapping("/common/quartzJob")
public class QuartzJobController extends BaseController {
	@Autowired
	private QuartzJobService quartzJobService;
	
	@GetMapping()
	@RequiresPermissions("quartz:quartzJob:quartzJob")
	String QuartzJob(){
	    return "/common/quartzJob/quartzJob";
	}
	
	@ResponseBody
	@PostMapping("/page")
	@RequiresPermissions("quartz:quartzJob:quartzJob")
	public DataTablePage page(HttpServletRequest request) {
		//组装成DataTables分页对象
		DataTablePage<QuartzJob> dataTablePage = new DataTablePage<QuartzJob>(request);
		//开始分页：PageHelper会处理接下来的第一个查询
		PageInfo<QuartzJob> pageInfo = quartzJobService.queryByPage(dataTablePage.getPageNum(), dataTablePage.getPageSize(), new QuartzJob());
		//封装数据给DataTables
		dataTablePage.setData(pageInfo.getList());
		dataTablePage.setRecordsTotal((int) pageInfo.getTotal());
		dataTablePage.setRecordsFiltered(dataTablePage.getRecordsTotal());
		return dataTablePage;
	}
	@RequiresPermissions("quartz:quartzJob:quartzJob")
	@RequestMapping("/list")
	@ResponseBody
	public Object list(QuartzJob quartzJob, HttpServletRequest request) {
		/**Sort对象初始化错误，需要更换方法解决*/
		ParameterModelBean bean = ParameterModelBeanUtil.getParameterModelBean(quartzJob);
		bean = parmeterPage(request,bean);
		if(StringUtils.isEmpty(bean.getOrder())){
			bean.setOrder("jobId" );
			bean.setSort("asc");
		}
		PageInfo<QuartzJob> pageInfo = quartzJobService.getPageList(bean);
		return pageInfo;
	}
	@GetMapping("/add")
	@RequiresPermissions("quartz:quartzJob:add")
	String add(Model model){
		/**只读属性*/
		model.addAttribute("readOnly", false);
		model.addAttribute("quartzJob", new QuartzJob());
	    return "/common/quartzJob/add";
	}

	@GetMapping("/edit/{jobId}")
	@RequiresPermissions("quartz:quartzJob:edit")
	String edit(@PathVariable("jobId") String jobId,Model model){
		QuartzJob quartzJob = quartzJobService.get(jobId);
		model.addAttribute("quartzJob", quartzJob);
		/**只读属性*/
		model.addAttribute("readOnly", false);
		return "/common/quartzJob/add";
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	public AjaxJson save( QuartzJob quartzJob){
		AjaxJson ajaxJson;
		try {
			//保存用户信息
			int num=quartzJobService.save(quartzJob);
			if(num>0){
				ajaxJson = new AjaxSuccess();
				ajaxJson.setData(quartzJob);
			}else{
				ajaxJson = new AjaxError();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxError();
		}
		return ajaxJson;
	}

	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("quartz:quartzJob:remove")
	public AjaxJson remove( String jobId){
		AjaxJson ajaxJson;
		try {
			int delNum=quartzJobService.delete(jobId);
			if(delNum>0){
				ajaxJson = new AjaxSuccess();
				ajaxJson.setMessage("删除成功！");
				return ajaxJson;
			}else{
				ajaxJson = new AjaxError();
				ajaxJson.setMessage("删除失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxError();
			ajaxJson.setMessage("删除失败！"+e.getMessage());
		}
		return ajaxJson;
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("quartz:quartzJob:batchRemove")
	public AjaxJson remove(@RequestParam("ids[]") String[] jobIds){
		AjaxJson ajaxJson;
		if (jobIds != null && jobIds.length > 0) {
			quartzJobService.delete(jobIds);
			ajaxJson = new AjaxSuccess();
			ajaxJson.setMessage("删除成功！");
		} else {
			ajaxJson = new AjaxError();
			ajaxJson.setMessage("删除失败！");
		}
		return ajaxJson;
	}
	@GetMapping("/nextTriggerTime")
	@ResponseBody
	public AjaxJson nextTriggerTime(String cron){
		List<String> list=new ArrayList<String>();
		try {
			CronExpression cronExpression=new CronExpression(cron);
			Date lastTime=new Date();
			for(int i=0;i<5;i++){
				lastTime=cronExpression.getNextInvalidTimeAfter(lastTime);
				if(ObjectUtils.isNotEmpty(lastTime)){
					list.add(DateUtils.formatDateTime(lastTime));
				}else{
					break;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		AjaxJson ajaxJson=new AjaxSuccess();
		ajaxJson.setData(list);
		return ajaxJson;
	}
}
