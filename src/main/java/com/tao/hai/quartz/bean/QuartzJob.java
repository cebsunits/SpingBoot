package com.tao.hai.quartz.bean;

import com.tao.hai.base.BaseDataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;



/**
 * 
 * 
 * @author sunits
 * @email 112433877@qq.com
 * @date 2020-06-28 16:06:37
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Table(name="sys_quartz_job")
public class QuartzJob extends BaseDataEntity<QuartzJob> {
	//任务ID
	@Id
	private String jobId;
	//任务名称
	@Column(name = "job_name")
	private String jobName;
	//任务分组
	@Column(name = "job_group")
	private String jobGroup;
	//任务状态 是否启动任务
	@Column(name = "job_status")
	private String jobStatus;
	//执行类型，单个还是定时
	@Column(name = "cron_type")
	private String cronType;
	//执行开始时间
	@Column(name = "start_time")
	private Date startTime;
	//cron表达式
	@Column(name = "cron_expression")
	private String cronExpression;
	//描述
	@Column(name = "description")
	private String description;
	//任务执行时调用哪个类的方法
	@Column(name = "bean_class")
	private String beanClass;
	//任务是否有状态
	@Column(name = "is_concurrent")
	private String isConcurrent;
}
