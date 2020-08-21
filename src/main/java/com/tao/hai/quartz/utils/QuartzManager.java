package com.tao.hai.quartz.utils;

import com.tao.hai.quartz.constants.Constant;
import com.tao.hai.quartz.bean.QuartzJob;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class QuartzManager {
    @Autowired
    Scheduler scheduler;
    /**
     * 添加job方法
     */
    public void addJob(QuartzJob scheduleJob) {
        try {
            Class<? extends QuartzJobBean> jobClass = (Class<? extends QuartzJobBean>) (Class.forName(scheduleJob.getBeanClass()).newInstance().getClass());
            // 任务名称和组构成任务key
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())
                    .withDescription(scheduleJob.getDescription())
                    .build();
            // 设置job参数

            /**判断是单次执行还是定时的*/
            // 使用cornTrigger规则
            Trigger trigger = null;
            if (scheduleJob.getCronType().equals(Constant.SIMPLE)) {
                Date startTime = scheduleJob.getStartTime();
                boolean isStartNow = false;
                if (startTime == null) {
                    isStartNow = true;
                } else {
                    if (startTime.before(new Date())) {
                        isStartNow = true;
                    }
                }
                /**执行一次情况*/
                if (isStartNow) {
                    trigger = TriggerBuilder.newTrigger()
                            .withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())
                            .withDescription(scheduleJob.getDescription())
                            .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1)
                                    .withIntervalInSeconds(1))
                            .startNow()
                            .build();
                } else {
                    trigger = TriggerBuilder.newTrigger()
                            .withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())
                            .withDescription(scheduleJob.getDescription())
                            .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1)
                                    .withIntervalInSeconds(1))
                            .startAt(startTime)
                            .build();
                }
            } else {
                /**定时*/
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())
                        .withDescription(scheduleJob.getDescription())
                        .withSchedule(CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression()))
                        .startNow()
                        .build();
            }
            // 把作业和触发器注册到任务调度中
            scheduler.scheduleJob(jobDetail, trigger);
            //启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新job
     */
    public void updateJob(QuartzJob scheduleJob) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
            /***/
            if (scheduleJob.getCronType().equals(Constant.SIMPLE)) {
                SimpleTrigger trigger = (SimpleTrigger) scheduler.getTrigger(triggerKey);
                Date startTime = scheduleJob.getStartTime();
                boolean isStartNow = false;
                if (startTime == null) {
                    isStartNow = true;
                } else {
                    if (startTime.before(new Date())) {
                        isStartNow = true;
                    }
                }
                /**执行一次情况*/
                if (isStartNow) {
                    trigger = trigger.getTriggerBuilder()
                            .withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())
                            .withDescription(scheduleJob.getDescription())
                            .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1)
                                    .withIntervalInSeconds(1))
                            .startNow()
                            .build();
                } else {
                    trigger = trigger.getTriggerBuilder()
                            .withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())
                            .withDescription(scheduleJob.getDescription())
                            .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1)
                                    .withIntervalInSeconds(1))
                            .startAt(startTime)
                            .build();
                }
                //重启触发器
                scheduler.rescheduleJob(triggerKey, trigger);
            } else {
                CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                /**定时*/
                trigger = trigger.getTriggerBuilder()
                        .withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())
                        .withDescription(scheduleJob.getDescription())
                        .withSchedule(CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression()))
                        .startNow()
                        .build();
                //重启触发器
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停一个Job
     */
    public void pauseJob(QuartzJob scheduleJob) {
        try {
            JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 恢复一个Job
     */
    public void resumeJob(QuartzJob scheduleJob) {
        try {
            JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一个Job
     */
    public void deleteJob(QuartzJob scheduleJob) {
        try {
            JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 立即执行一个Job
     */
    public void runNow(QuartzJob scheduleJob) {
        try {
            JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /***获取所有计划任务*/
    public List<QuartzJob> queryAllJob() {
        List<QuartzJob> list = new ArrayList<>();
        try {
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            for (JobKey jobKey : jobKeys) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger ti : triggers) {
                    QuartzJob job = new QuartzJob();
                    job.setJobName(jobKey.getName());
                    job.setJobGroup(jobKey.getGroup());
                    job.setDescription(ti.getDescription());
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(ti.getKey());
                    job.setJobStatus(triggerState.name());
                    if (ti instanceof SimpleTrigger) {
                        job.setCronType(Constant.SIMPLE);
                        job.setStartTime(ti.getStartTime());
                    } else if (ti instanceof CronTrigger) {
                        job.setCronType(Constant.CRON);
                        job.setCronExpression(((CronTrigger) ti).getCronExpression());
                    }
                    ti.getJobDataMap();
                    list.add(job);
                }
            }

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return list;
    }

    /***获取所有正在运行的计划任务*/
    public List<QuartzJob> queryAllRunningJob() {
        List<QuartzJob> list = new ArrayList<>();
        try {
            List<JobExecutionContext> jobExecutionContexts = scheduler.getCurrentlyExecutingJobs();
            for (JobExecutionContext jobExecutionContext : jobExecutionContexts) {
                JobDetail jobDetail=jobExecutionContext.getJobDetail();
                JobKey jobKey=jobDetail.getKey();
                Trigger trigger=jobExecutionContext.getTrigger();
                QuartzJob job = new QuartzJob();
                job.setJobName(jobKey.getName());
                job.setJobGroup(jobKey.getGroup());
                job.setDescription(jobDetail.getDescription());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.setJobStatus(triggerState.name());
                if (trigger instanceof SimpleTrigger) {
                    job.setCronType(Constant.SIMPLE);
                    job.setStartTime(trigger.getStartTime());
                } else if (trigger instanceof CronTrigger) {
                    job.setCronType(Constant.CRON);
                    job.setCronExpression(((CronTrigger) trigger).getCronExpression());
                }
                trigger.getJobDataMap();
                list.add(job);
            }

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return list;
    }

}
