package com.tao.hai.quartz.service.impl;

import com.tao.hai.base.BaseServiceImpl;
import com.tao.hai.quartz.bean.QuartzJob;
import com.tao.hai.quartz.dao.QuartzJobDao;
import com.tao.hai.quartz.service.QuartzJobService;
import com.tao.hai.util.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;


@Service
public class QuartzJobServiceImpl extends BaseServiceImpl<QuartzJobDao, QuartzJob> implements QuartzJobService {
	@Autowired
	private QuartzJobDao quartzJobDao;

	/**
    * 保存方法
    */
	@Override
	public int save(QuartzJob quartzJob) {
		if (StringUtils.isEmpty(quartzJob.getJobId())) {
			quartzJob.setJobId(UUID.randomUUID().toString());
			quartzJob.setCreateDate(new Date());
			quartzJob.setCreateId(ShiroUtils.getUserId());
		} else {
			quartzJob.setNewRecord(false);
		}
		quartzJob.setUpdateDate(new Date());
		quartzJob.setUpdateId(ShiroUtils.getUserId());
		return super.save(quartzJob);
	}

	@Override
	public QuartzJob get(String jobId){
		QuartzJob quartzJob = new QuartzJob();
		quartzJob.setJobId(jobId);
		quartzJob = super.getByKey(quartzJob);
		return quartzJob;
	}
	
}
