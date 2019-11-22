package com.cognition.bit.system.controller;

import com.cognition.bit.system.entity.SysMenu;
import com.cognition.bit.system.service.MenuService;
import com.cognition.bit.common.config.Constant;
import com.cognition.bit.common.until.Query;
import com.cognition.bit.common.until.ResultData;
import com.cognition.bit.common.until.StringUtils;
import com.cognition.bit.system.persistence.BaseController;
import com.cognition.bit.system.persistence.Tree;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 菜单控制
 * @author taoya
 */
@Controller
@RequestMapping("/system/menu")
public class MenuController extends BaseController {


	private MenuService menuService;

	@Autowired
	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}


	@RequiresPermissions("sys:menu:menu")
	@GetMapping()
	String menu(Model model) {
		return "system/menu/menu";
	}

	/**
	 * @param params
	 * @return
	 */
	@RequiresPermissions("sys:menu:menu")
	@RequestMapping("/list")
	@ResponseBody
	List<SysMenu> list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Map<String, Object> query = Query.withDelFlag();
		List<SysMenu> sysMenuList = menuService.findList(query);
		return sysMenuList;
	}

	//	@Log("添加菜单")
	@RequiresPermissions("sys:menu:add")
	@GetMapping("/add/{pId}")
	String add(Model model, @PathVariable("pId")String pId) {
	    long pid = 0;
		if (!StringUtils.isEmpty(pId)){
	    	pid = Long.parseLong(pId);
        }
		model.addAttribute("pId", pid);
		if (pid == Constant.INT_ZERO) {
			model.addAttribute("pName", "根目录");
		} else {
			model.addAttribute("pName", menuService.get(pid).getName());
		}
		return "system/menu/add";
	}

	//	@Log("编辑菜单")
	@RequiresPermissions("sys:menu:edit")
	@GetMapping("/edit/{id}")
	String edit(Model model, @PathVariable("id")Long id) {
		SysMenu sysMenu = menuService.get(id);
		Long parentId = sysMenu.getParentId();
		model.addAttribute("pId", parentId);
		if (parentId.equals(Constant.STRING_ZERO)) {
			model.addAttribute("pName", "根目录");
		} else {
			model.addAttribute("pName", menuService.get(parentId).getName());
		}
		model.addAttribute("menu", sysMenu);
		return "system/menu/edit";
	}

	//	@Log("保存菜单")
	@RequiresPermissions("sys:menu:add")
	@PostMapping("/save")
	@ResponseBody
    ResultData save(SysMenu sysMenu) {
		//根据权限存入创建者
		sysMenu.setCreateBy(getUserId());
		if (menuService.save(sysMenu) > 0) {
			return ResultData.result(true);
		} else {
			return ResultData.result(false, "保存失败");
		}
	}

	//	@Log("更新菜单")
	@RequiresPermissions("sys:menu:edit")
	@PostMapping("/update")
	@ResponseBody
    ResultData update(SysMenu sysMenu) {
		if (menuService.update(sysMenu) > 0) {
			return ResultData.result(true);
		} else {
			return ResultData.result(false, "更新失败");
		}
	}

	//	@Log("删除菜单")
	@RequiresPermissions("sys:menu:remove")
	@PostMapping("/remove")
	@ResponseBody
    ResultData remove(Long id) {
		if (menuService.delete(id) > 0) {
			return ResultData.result(true);
		} else {
			return ResultData.result(false, "删除失败");
		}
	}

	@GetMapping("/tree")
	@ResponseBody
	Tree<SysMenu> tree() {
		return menuService.getTree();
	}


	@GetMapping("/tree/{roleId}")
	@ResponseBody
	Tree<SysMenu> tree(@PathVariable("roleId") String roleId) {
		Tree<SysMenu> tree = menuService.getTree(roleId);
		return tree;
	}

}
