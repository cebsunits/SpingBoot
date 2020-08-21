package com.tao.hai.quartz.dao;

import com.tao.hai.base.BaseDao;
import com.tao.hai.quartz.bean.QuartzJob;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author sunits
 * @email 112433877@qq.com
 * @date 2020-06-28 16:06:37
 */
@Mapper
public interface QuartzJobDao extends BaseDao<QuartzJob> {

	QuartzJob get(String jobId);

}
