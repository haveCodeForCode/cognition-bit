package com.cognition.bit.modules.service;


import com.cognition.bit.modules.entity.ModTask;
import org.quartz.SchedulerException;

import java.util.List;
import java.util.Map;


/**
 *
 * 定时任务
 * @author taoya
 */
public interface TaskJobService {

	ModTask get(Long id);

	List<ModTask> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(ModTask modTaskScheduleJob);

	int update(ModTask modTaskScheduleJob);

	int remove(Long id);

	int batchRemove(Long[] ids);

	void initSchedule() throws SchedulerException;

	void changeStatus(Long jobId, String cmd) throws SchedulerException;

	void updateCron(Long jobId) throws SchedulerException;
}
