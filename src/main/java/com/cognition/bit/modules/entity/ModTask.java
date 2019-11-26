package com.cognition.bit.modules.entity;

import com.cognition.bit.system.persistence.BaseEntity;

import java.io.Serializable;

/**
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-25 15:09:21
 */
public class ModTask extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    /** cron表达式*/
    private String cronExpression;
    /** 任务调用的方法名*/
    private String methodName;
    /** 任务是否有状态*/
    private String isConcurrent;
    /** 任务描述*/
    private String description;
    /** 任务执行时调用哪个类的方法 包名+类名*/
    private String beanClass;
    /** 任务状态*/
    private String jobStatus;
    /** 任务分组*/
    private String jobGroup;
    /** Spring bean*/
    private String springBean;
    /** 任务名*/
    private String jobName;


    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getIsConcurrent() {
        return isConcurrent;
    }

    public void setIsConcurrent(String isConcurrent) {
        this.isConcurrent = isConcurrent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getSpringBean() {
        return springBean;
    }

    public void setSpringBean(String springBean) {
        this.springBean = springBean;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public String toString() {
        return "Task{" +
                "cronExpression='" + cronExpression + '\'' +
                ", methodName='" + methodName + '\'' +
                ", isConcurrent='" + isConcurrent + '\'' +
                ", description='" + description + '\'' +
                ", beanClass='" + beanClass + '\'' +
                ", jobStatus='" + jobStatus + '\'' +
                ", jobGroup='" + jobGroup + '\'' +
                ", springBean='" + springBean + '\'' +
                ", jobName='" + jobName + '\'' +
                ", id=" + id +
                ", createBy=" + createBy +
                ", updateBy=" + updateBy +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
