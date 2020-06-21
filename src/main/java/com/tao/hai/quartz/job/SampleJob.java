package com.tao.hai.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
@Slf4j
public class SampleJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        /**获取参数*/
        JobDataMap jobDataMap=jobExecutionContext.getJobDetail().getJobDataMap();
        /**业务逻辑*/
        log.info("test"+jobDataMap.toString());
    }
}
