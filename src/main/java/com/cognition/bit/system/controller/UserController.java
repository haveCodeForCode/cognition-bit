package com.cognition.bit.system.controller;


import com.cognition.bit.system.domain.SysDept;
import com.cognition.bit.system.domain.SysRole;
import com.cognition.bit.system.domain.SysUser;
import com.cognition.bit.system.service.RoleService;
import com.cognition.bit.system.service.UserService;
import com.cognition.bit.common.until.PageUtils;
import com.cognition.bit.common.until.Query;
import com.cognition.bit.common.until.ResultData;
import com.cognition.bit.modules.service.DictService;
import com.cognition.bit.system.persistence.BaseController;
import com.cognition.bit.system.persistence.Tree;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *
 * @author taoya
 */
@RequestMapping("/system/user")
@Controller
public class UserController extends BaseController {


	private UserService userService;

	private RoleService roleService;

	private DictService dictService;

	@Autowired
	public void setUserService(UserService userService){this.userService = userService;}

	@Autowired
	public void setRoleService(RoleService roleService){this.roleService = roleService;}

	@Autowired
	public void setDictService(DictService dictService) {
		this.dictService = dictService;
	}

	@RequiresPermissions("sys:user:user")
	@GetMapping("")
	String user(Model model) {
		return "system/user/user";
	}

	@GetMapping("/list")
	@ResponseBody
	PageUtils list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);
		List<SysUser> sysSysUserList = userService.list(query);
		int total = userService.count(query);
		PageUtils pageUtil = new PageUtils(sysSysUserList, total);
		return pageUtil;
	}

	@RequiresPermissions("sys:user:add")
//	@Log("添加用户")
	@GetMapping("/add")
	String add(Model model) {
		List<SysRole> sysRoles = roleService.list(getUser().getId());
		model.addAttribute("roles", sysRoles);
		return "system/user/add";
	}

	@RequiresPermissions("sys:user:edit")
//	@Log("编辑用户")
	@GetMapping("/edit/{id}")
	String edit(Model model, @Param("id") Long id) {
		SysUser sysUser = userService.get(id);
		model.addAttribute("user", sysUser);
		List<SysRole> sysRoles = roleService.list(id);
		model.addAttribute("roles", sysRoles);
		return "system/user/edit";
	}

	@RequiresPermissions("sys:user:add")
//	@Log("保存用户")
	@PostMapping("/save")
	@ResponseBody
    ResultData save(SysUser sysUser) {
//		user.setUserPassword(Md5Utils.encrypt(user.getUsername(), user.getPassword()));
		if (userService.save(sysUser) > 0) {
			return ResultData.result(true);
		}
		return ResultData.result(false);
	}

	@RequiresPermissions("sys:user:edit")
//	@Log("更新用户")
	@PostMapping("/update")
	@ResponseBody
    ResultData update(SysUser sysUser) {
		if (userService.update(sysUser) > 0) {
			return ResultData.result(true);
		}
		return ResultData.result(false);
	}


	@RequiresPermissions("sys:user:edit")
//	@Log("更新用户")
	@PostMapping("/updatePeronal")
	@ResponseBody
    ResultData updatePeronal(SysUser sysUser) {
		if (userService.updatePersonal(sysUser) > 0) {
			return ResultData.result(true);
		}
		return ResultData.result(false);
	}


	@RequiresPermissions("sys:user:remove")
//	@Log("删除用户")
	@PostMapping("/remove")
	@ResponseBody
    ResultData remove(Long id) {
		if (userService.delete(id) > 0) {
			return ResultData.result(true);
		}
		return ResultData.result(false);
	}

	@RequiresPermissions("sys:user:batchRemove")
//	@Log("批量删除用户")
	@PostMapping("/batchRemove")
	@ResponseBody
    ResultData batchRemove(@RequestParam("ids[]") Long[] userIds) {
		int r = userService.batchDelete(userIds);
		if (r > 0) {
			return ResultData.result(true);
		}
		return ResultData.result(false);
	}

	@PostMapping("/exit")
	@ResponseBody
	boolean exit(@RequestParam Map<String, Object> params) {
		// 存在，不通过，false
		return !userService.exit(params);
	}

	@RequiresPermissions("sys:user:resetPwd")
//	@Log("请求更改用户密码")
	@GetMapping("/resetPwd/{id}")
	String resetPwd(@RequestParam("id") Long userId, Model model) {
		SysUser sysUser = new SysUser();
		sysUser.setId(userId);
		model.addAttribute("user", sysUser);
		return "system/user/reset_pwd";
	}

//	@Log("提交更改用户密码")
//	@PostMapping("/resetPwd")
//	@ResponseBody
//	R resetPwd(UserVO userVO) {
//		try{
//			userService.resetPwd(userVO,getUser());
//			return R.ok();
//		}catch (Exception e){
//			return R.error(1,e.getMessage());
//		}
//
//	}
//	@RequiresPermissions("sys:user:resetPwd")
//	@Log("admin提交更改用户密码")
//	@PostMapping("/adminResetPwd")
//	@ResponseBody
//	R adminResetPwd(UserVO userVO) {
//		try{
//			userService.adminResetPwd(userVO);
//			return R.ok();
//		}catch (Exception e){
//			return R.error(1,e.getMessage());
//		}
//
//	}

	@GetMapping("/tree")
	@ResponseBody
	public Tree<SysDept> tree() {
		return userService.getTree();
	}

	@GetMapping("/treeView")
	String treeView() {
		return "system/user/userTree";
	}


//	@ResponseBody
//	@PostMapping("/uploadImg")
//	R uploadImg(@RequestParam("avatar_file") MultipartFile file, String avatar_data, HttpServletRequest request) {
//		Map<String, Object> result = new HashMap<>();
//		try {
//			result = userService.updatePersonalImg(file, avatar_data, getUserId());
//		} catch (Exception e) {
//			return R.error("更新图像失败！");
//		}
//		if(result!=null && result.size()>0){
//			return R.ok(result);
//		}else {
//			return R.error("更新图像失败！");
//		}
//	}
}
