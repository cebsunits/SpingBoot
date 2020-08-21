package com.tao.hai.quartz.service;

import com.tao.hai.base.BaseService;
import com.tao.hai.quartz.bean.QuartzJob;

/**
 * 
 * 
 * @author sunits
 * @email 112433877@qq.com
 * @date 2020-06-28 16:06:37
 */
public interface QuartzJobService extends BaseService<QuartzJob>  {
	
	QuartzJob get(String jobId);
}
