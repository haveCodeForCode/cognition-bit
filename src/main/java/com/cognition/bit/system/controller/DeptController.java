package com.cognition.bit.system.controller;


import com.cognition.bit.system.domain.SysDept;
import com.cognition.bit.system.persistence.BaseController;
import com.cognition.bit.system.service.DeptService;
import com.cognition.bit.common.config.Constant;
import com.cognition.bit.common.until.Query;
import com.cognition.bit.common.until.ResultData;
import com.cognition.bit.system.persistence.Tree;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门管理
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-27 14:40:36
 */

@Controller
@RequestMapping("/system/sysDept")
public class DeptController extends BaseController {

	private DeptService deptService;

	@Autowired
	public void setDeptService(DeptService deptService) {
		this.deptService = deptService;
	}


	@GetMapping()
	@RequiresPermissions("system:dept:sysDept")
	String dept() {
		return "system/dept/dept";
	}

//	@ApiOperation(value="获取部门列表", notes="")
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("system:dept:sysDept")
	public List<SysDept> list() {
		Map<String, Object> query = Query.withDelFlag();
		List<SysDept> sysDeptList = deptService.findList(query);
		for (SysDept sysDept : sysDeptList) {
			if (sysDept.getParentId() != null) {
				for (SysDept sysDeptOne : sysDeptList) {
					if (sysDeptOne.getId().equals(sysDept.getParentId())) {
						sysDept.setName(sysDeptOne.getName());
					}
				}
			}
		}
		return sysDeptList;
	}

	@GetMapping("/add/{pId}")
	@RequiresPermissions("system:dept:add")
	String add(@PathVariable("pId")Long pId, Model model) {
		model.addAttribute("pId", pId);
		if (pId == Constant.INT_ZERO) {
			model.addAttribute("pName", "总部门");
		} else {
			model.addAttribute("pName", deptService.get(pId).getName());
		}
		return "system/dept/add";
	}

	@GetMapping("/edit/{deptId}")
	@RequiresPermissions("system:dept:edit")
	String edit(@Param("deptId") Long deptId, Model model) {
		SysDept sysDept = deptService.get(deptId);
		model.addAttribute("sysDept", sysDept);
		SysDept parSysDept = deptService.get(sysDept.getParentId());
		model.addAttribute("parentDeptName", parSysDept.getName());
		return "system/dept/edit";
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("system:dept:add")
	public ResultData save(SysDept sysDept) {
		//根据权限存放的
		sysDept.setCreateBy(getUserId());
		if (deptService.save(sysDept) > 0) {
			return ResultData.result(true);
		}
		return ResultData.result(false);
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("system:dept:edit")
	public ResultData update(SysDept sysDept) {
		if (deptService.update(sysDept) > 0) {
			return ResultData.result(true);
		}
		return ResultData.result(false);
	}

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	@RequiresPermissions("system:dept:remove")
	public ResultData remove(Long deptId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parentId", deptId);
		if(deptService.count(map)>0) {
			return ResultData.result(false, "包含下级部门,不允许修改");
		}
		if(deptService.checkDeptHasUser(deptId)) {
			if (deptService.delete(deptId) > 0) {
				return ResultData.result(false);
			}
		}else {
			return ResultData.result(false, "部门包含用户,不允许修改");
		}
		return ResultData.result(false);
	}

	/**
	 * 删除
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	@RequiresPermissions("system:dept:batchRemove")
	public ResultData remove(@RequestParam("ids[]") Long[] deptIds) {
		deptService.batchDelete(deptIds);
		return ResultData.result(true);
	}

	@GetMapping("/tree")
	@ResponseBody
	public Tree<SysDept> tree() {
		Tree<SysDept> tree = new Tree<SysDept>();
		tree = deptService.getTree();
		return tree;
	}

	@GetMapping("/treeView")
	String treeView() {
		return "system/dept/deptTree";
	}

}
