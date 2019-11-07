package com.root.cognition.modules.controller;


import com.root.cognition.common.until.ResultData;
import com.root.cognition.system.persistence.BaseController;
import com.root.cognition.common.until.PageUtils;
import com.root.cognition.common.until.Query;
import com.root.cognition.modules.entity.Task;
import com.root.cognition.modules.service.TaskJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 定时任务前端控制器
 * @author 1024
 * @date 2017-09-26 20:53:48
 */
@Controller
@RequestMapping("/modules/job")
public class JobController extends BaseController {

	private TaskJobService taskScheduleJobService;

	@Autowired
	public void setTaskScheduleJobService(TaskJobService taskScheduleJobService) {
		this.taskScheduleJobService = taskScheduleJobService;
	}

	@GetMapping()
	String taskScheduleJob() {
		return "modules/job/job";
	}

	@ResponseBody
	@GetMapping("/list")
	public PageUtils list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);
		List<Task> taskScheduleJobList = taskScheduleJobService.list(query);
		int total = taskScheduleJobService.count(query);
		PageUtils pageUtils = new PageUtils(taskScheduleJobList, total);
		return pageUtils;
	}

	@GetMapping("/add")
	String add() {
		return "modules/job/add";
	}

	@GetMapping("/edit/{id}")
	String edit(@PathVariable("id") Long id, Model model) {
		Task job = taskScheduleJobService.get(id);
		model.addAttribute("job", job);
		return "modules/job/edit";
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	public ResultData info(@PathVariable("id") Long id) {
		Task taskScheduleJob = taskScheduleJobService.get(id);
		return ResultData.result(true, taskScheduleJob);
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	public ResultData save(Task taskScheduleJob) {
		if (taskScheduleJobService.save(taskScheduleJob) > 0) {
			return ResultData.result(true);
		}
		return ResultData.result(false);
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@PostMapping("/update")
	public ResultData update(Task taskScheduleJob) {
		taskScheduleJobService.update(taskScheduleJob);
		return ResultData.result(true);
	}

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	public ResultData remove(Long id) {
		if (taskScheduleJobService.remove(id) > 0) {
			return ResultData.result(true);
		}
		return ResultData.result(false);
	}

	/**
	 * 删除
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	public ResultData remove(@RequestParam("ids[]") Long[] ids) {
		taskScheduleJobService.batchRemove(ids);
		return ResultData.result(true);
	}

	@PostMapping(value = "/changeJobStatus")
	@ResponseBody
	public ResultData changeJobStatus(Long id, String cmd ) {
		String label = "停止";
		if ("start".equals(cmd)) {
			label = "启动";
		} else {
			label = "停止";
		}
		try {
			taskScheduleJobService.changeStatus(id, cmd);
			return ResultData.result(true,"任务" + label + "成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultData.result(false,"任务" + label + "失败");
	}

}
