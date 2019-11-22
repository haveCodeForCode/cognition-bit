package com.cognition.bit.modules.config;

import com.cognition.bit.modules.entity.ModScheduleJob;
import org.quartz.*;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @title: QuartzManager.java
 * @description: 计划任务管理
 */
@Service
public class QuartzManager {
    public final Logger log = LoggerFactory.getLogger(QuartzManager.class);

    private Scheduler scheduler;

    @Autowired
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * 添加任务
     *
     * @param modScheduleJob
     * @throws SchedulerException
     */

    public void addJob(ModScheduleJob modScheduleJob) {
        try {
            // 创建jobDetail实例，绑定Job实现类
            // 指明job的名称，所在组的名称，以及绑定job类
            Class<? extends Job> jobClass = (Class<? extends Job>) (Class.forName(modScheduleJob.getBeanClass()).newInstance().getClass());
            // 任务名称和组构成任务key
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(modScheduleJob.getJobName(), modScheduleJob.getJobGroup()).build();
            // 定义调度触发规则
            // 使用cornTrigger规则
            // 触发器key
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(modScheduleJob.getJobName(), modScheduleJob.getJobGroup())
                    .startAt(DateBuilder.futureDate(1, IntervalUnit.SECOND))
                    .withSchedule(CronScheduleBuilder.cronSchedule(modScheduleJob.getCronExpression())).startNow().build();
            // 把作业和触发器注册到任务调度中
            scheduler.scheduleJob(jobDetail, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有计划中的任务列表
     *
     * @return
     * @throws SchedulerException
     */
    public List<ModScheduleJob> getAllJob() throws SchedulerException {
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<ModScheduleJob> jobList = new ArrayList<ModScheduleJob>();
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
                ModScheduleJob job = new ModScheduleJob();
                job.setJobName(jobKey.getName());
                job.setJobGroup(jobKey.getGroup());
                job.setDescription("触发器:" + trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.setJobStatus(triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    job.setCronExpression(cronExpression);
                }
                jobList.add(job);
            }
        }
        return jobList;
    }

    /**
     * 所有正在运行的job
     *
     * @return
     * @throws SchedulerException
     */
    public List<ModScheduleJob> getRunningJob() throws SchedulerException {
        List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
        List<ModScheduleJob> jobList = new ArrayList<ModScheduleJob>(executingJobs.size());
        for (JobExecutionContext executingJob : executingJobs) {
            ModScheduleJob job = new ModScheduleJob();
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            job.setJobName(jobKey.getName());
            job.setJobGroup(jobKey.getGroup());
            job.setDescription("触发器:" + trigger.getKey());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            job.setJobStatus(triggerState.name());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                job.setCronExpression(cronExpression);
            }
            jobList.add(job);
        }
        return jobList;
    }

    /**
     * 暂停一个job
     *
     * @param modScheduleJob
     * @throws SchedulerException
     */
    public void pauseJob(ModScheduleJob modScheduleJob) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(modScheduleJob.getJobName(), modScheduleJob.getJobGroup());
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复一个job
     *
     * @param modScheduleJob
     * @throws SchedulerException
     */
    public void resumeJob(ModScheduleJob modScheduleJob) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(modScheduleJob.getJobName(), modScheduleJob.getJobGroup());
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除一个job
     *
     * @param modScheduleJob
     * @throws SchedulerException
     */
    public void deleteJob(ModScheduleJob modScheduleJob) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(modScheduleJob.getJobName(), modScheduleJob.getJobGroup());
        scheduler.deleteJob(jobKey);

    }

    /**
     * 立即执行job
     *
     * @param modScheduleJob
     * @throws SchedulerException
     */
    public void runAJobNow(ModScheduleJob modScheduleJob) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(modScheduleJob.getJobName(), modScheduleJob.getJobGroup());
        scheduler.triggerJob(jobKey);
    }

    /**
     * 更新job时间表达式
     *
     * @param modScheduleJob
     * @throws SchedulerException
     */
    public void updateJobCron(ModScheduleJob modScheduleJob) throws SchedulerException {

        TriggerKey triggerKey = TriggerKey.triggerKey(modScheduleJob.getJobName(), modScheduleJob.getJobGroup());

        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(modScheduleJob.getCronExpression());

        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

        scheduler.rescheduleJob(triggerKey, trigger);
    }
}
