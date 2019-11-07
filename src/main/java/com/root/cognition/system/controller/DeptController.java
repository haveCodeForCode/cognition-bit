package com.root.cognition.system.controller;


import com.root.cognition.common.config.Constant;
import com.root.cognition.common.until.Query;
import com.root.cognition.common.until.ResultData;
import com.root.cognition.system.entity.Dept;
import com.root.cognition.system.persistence.BaseController;
import com.root.cognition.system.persistence.Tree;
import com.root.cognition.system.service.DeptService;
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
	public List<Dept> list() {
		Map<String, Object> query = Query.withDelFlag();
		List<Dept> deptList = deptService.findList(query);
		for (Dept dept : deptList) {
			if (dept.getParentId() != null) {
				for (Dept deptOne : deptList) {
					if (deptOne.getId().equals(dept.getParentId())) {
						dept.setName(deptOne.getName());
					}
				}
			}
		}
		return deptList;
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
		Dept dept = deptService.get(deptId);
		model.addAttribute("sysDept", dept);
		Dept parDept = deptService.get(dept.getParentId());
		model.addAttribute("parentDeptName", parDept.getName());
		return "system/dept/edit";
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("system:dept:add")
	public ResultData save(Dept dept) {
		//根据权限存放的
		dept.setCreateBy(getUserId());
		if (deptService.save(dept) > 0) {
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
	public ResultData update(Dept dept) {
		if (deptService.update(dept) > 0) {
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
	public Tree<Dept> tree() {
		Tree<Dept> tree = new Tree<Dept>();
		tree = deptService.getTree();
		return tree;
	}

	@GetMapping("/treeView")
	String treeView() {
		return "system/dept/deptTree";
	}

}
