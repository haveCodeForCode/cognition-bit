package com.cognition.bit.framework.service.impl;


import com.cognition.bit.framework.config.QuartzManager;
import com.cognition.bit.framework.dao.TaskDao;
import com.cognition.bit.framework.entity.ModScheduleJob;
import com.cognition.bit.framework.entity.ModTask;
import com.cognition.bit.common.config.Constant;
import com.cognition.bit.common.until.ScheduleJobUtils;
import com.cognition.bit.framework.service.TaskJobService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定时任务实现类
 * @author taoya
 */
@Service
public class TaskJobServiceImpl implements TaskJobService {


	private TaskDao taskScheduleJobMapper;

	private QuartzManager quartzManager;

	@Autowired
	public void setQuartzManager(QuartzManager quartzManager) {
		this.quartzManager = quartzManager;
	}

	@Autowired
	public void setTaskScheduleJobMapper(TaskDao taskScheduleJobMapper) {
		this.taskScheduleJobMapper = taskScheduleJobMapper;
	}

	@Override
	public ModTask get(Long id) {
		return taskScheduleJobMapper.get(id);
	}

	@Override
	public List<ModTask> list(Map<String, Object> map) {
		return taskScheduleJobMapper.findList(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return taskScheduleJobMapper.count(map);
	}

	@Override
	public int save(ModTask modTaskScheduleJob) {
		modTaskScheduleJob.preInsert();
		return taskScheduleJobMapper.insert(modTaskScheduleJob);
	}

	@Override
	public int update(ModTask modTaskScheduleJob) {
		return taskScheduleJobMapper.update(modTaskScheduleJob);
	}

	@Override
	public int remove(Long id) {
		try {
			ModTask scheduleJob = get(id);
			quartzManager.deleteJob(ScheduleJobUtils.entityToData(scheduleJob));
			return taskScheduleJobMapper.remove(id);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return 0;
		}

	}

	@Override
	public int batchRemove(Long[] ids) {
		for (Long id : ids) {
			try {
				ModTask scheduleJob = get(id);
				quartzManager.deleteJob(ScheduleJobUtils.entityToData(scheduleJob));
			} catch (SchedulerException e) {
				e.printStackTrace();
				return 0;
			}
		}
		return taskScheduleJobMapper.batchRemove(ids);
	}

	@Override
	public void initSchedule() throws SchedulerException {
		// 这里获取任务信息数据
		List<ModTask> jobList = taskScheduleJobMapper.findList(new HashMap<String, Object>(16));
		for (ModTask scheduleJob : jobList) {
			if ("1".equals(scheduleJob.getJobStatus())) {
				ModScheduleJob job = ScheduleJobUtils.entityToData(scheduleJob);
				quartzManager.addJob(job);
			}

		}
	}

	@Override
	public void changeStatus(Long jobId, String cmd) throws SchedulerException {
		ModTask scheduleJob = get(jobId);
		if (scheduleJob == null) {
			return;
		}
		if (Constant.STATUS_RUNNING_STOP.equals(cmd)) {
			quartzManager.deleteJob(ScheduleJobUtils.entityToData(scheduleJob));
			scheduleJob.setJobStatus(ModScheduleJob.STATUS_NOT_RUNNING);
		} else {
			if (!Constant.STATUS_RUNNING_START.equals(cmd)) {
			} else {
                scheduleJob.setJobStatus(ModScheduleJob.STATUS_RUNNING);
                quartzManager.addJob(ScheduleJobUtils.entityToData(scheduleJob));
            }
		}
		update(scheduleJob);
	}

	@Override
	public void updateCron(Long jobId) throws SchedulerException {
		ModTask scheduleJob = get(jobId);
		if (scheduleJob == null) {
			return;
		}
		if (ModScheduleJob.STATUS_RUNNING.equals(scheduleJob.getJobStatus())) {
			quartzManager.updateJobCron(ScheduleJobUtils.entityToData(scheduleJob));
		}
		update(scheduleJob);
	}

}
