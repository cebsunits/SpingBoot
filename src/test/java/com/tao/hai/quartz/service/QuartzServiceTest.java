package com.tao.hai.quartz.service;

import com.tao.hai.quartz.bean.QuartzJob;
import com.tao.hai.quartz.utils.QuartzManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuartzServiceTest {
    @Resource
    QuartzManager quartzService;
    @Test
    public void jobTest(){
        QuartzJob job=new QuartzJob();
        job.setJobId("0001");
        job.setJobName("job");
        job.setJobGroup("DEFAULT");
        job.setCronType("0");
        job.setDescription("job simple");
        job.setBeanClass("com.tao.hai.quartz.job.SampleJob");
        quartzService.addJob(job);

        //quartzService.deleteJob(job);
    }
    @Test
    public void jobPauseTest(){
        QuartzJob job=new QuartzJob();
        job.setJobId("0001");
        job.setJobName("job");
        job.setJobGroup("DEFAULT");
        job.setCronType("0");
        job.setDescription("job simple");
        job.setBeanClass("com.tao.hai.quartz.job.SampleJob");
        quartzService.pauseJob(job);
    }
    @Test
    public void jobResumeTest(){
        QuartzJob job=new QuartzJob();
        job.setJobId("0001");
        job.setJobName("job");
        job.setJobGroup("DEFAULT");
        job.setCronType("0");
        job.setDescription("job simple");
        job.setBeanClass("com.tao.hai.quartz.job.SampleJob");
        quartzService.resumeJob(job);
    }
    @Test
    public void jobDeleteTest(){
        QuartzJob job=new QuartzJob();
        job.setJobId("0001");
        job.setJobName("job");
        job.setJobGroup("DEFAULT");
        job.setCronType("0");
        job.setDescription("job simple");
        job.setBeanClass("com.tao.hai.quartz.job.SampleJob");
        quartzService.deleteJob(job);
    }
}
