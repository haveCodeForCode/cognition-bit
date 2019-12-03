package com.cognition.bit.common.until;

import com.cognition.bit.framework.entity.ModScheduleJob;
import com.cognition.bit.framework.entity.ModTask;

/**
 * 定时任务工具类
 * @author taoya
 */
public class ScheduleJobUtils {

	/**
	 * 转换为定时任务可用实体
	 * @param scheduleJobEntity
	 * @return
	 */
	public static ModScheduleJob entityToData(ModTask scheduleJobEntity) {
		ModScheduleJob modScheduleJob = new ModScheduleJob();
		modScheduleJob.setBeanClass(scheduleJobEntity.getBeanClass());
		modScheduleJob.setCronExpression(scheduleJobEntity.getCronExpression());
		modScheduleJob.setDescription(scheduleJobEntity.getDescription());
		modScheduleJob.setIsConcurrent(scheduleJobEntity.getIsConcurrent());
		modScheduleJob.setJobName(scheduleJobEntity.getJobName());
		modScheduleJob.setJobGroup(scheduleJobEntity.getJobGroup());
		modScheduleJob.setJobStatus(scheduleJobEntity.getJobStatus());
		modScheduleJob.setMethodName(scheduleJobEntity.getMethodName());
		modScheduleJob.setSpringBean(scheduleJobEntity.getSpringBean());
		return modScheduleJob;
	}
}
