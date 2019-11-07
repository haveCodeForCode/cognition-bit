package com.root.cognition.system.controller;

import com.root.cognition.common.config.Constant;
import com.root.cognition.common.until.Query;
import com.root.cognition.common.until.ResultData;
import com.root.cognition.common.until.StringUtils;
import com.root.cognition.system.entity.Menu;
import com.root.cognition.system.persistence.BaseController;
import com.root.cognition.system.persistence.Tree;
import com.root.cognition.system.service.MenuService;
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
	List<Menu> list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Map<String, Object> query = Query.withDelFlag();
		List<Menu> menuList = menuService.findList(query);
		return menuList;
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
		Menu menu = menuService.get(id);
		Long parentId = menu.getParentId();
		model.addAttribute("pId", parentId);
		if (parentId.equals(Constant.STRING_ZERO)) {
			model.addAttribute("pName", "根目录");
		} else {
			model.addAttribute("pName", menuService.get(parentId).getName());
		}
		model.addAttribute("menu", menu);
		return "system/menu/edit";
	}

	//	@Log("保存菜单")
	@RequiresPermissions("sys:menu:add")
	@PostMapping("/save")
	@ResponseBody
    ResultData save(Menu menu) {
		//根据权限存入创建者
		menu.setCreateBy(getUserId());
		if (menuService.save(menu) > 0) {
			return ResultData.result(true);
		} else {
			return ResultData.result(false, "保存失败");
		}
	}

	//	@Log("更新菜单")
	@RequiresPermissions("sys:menu:edit")
	@PostMapping("/update")
	@ResponseBody
    ResultData update(Menu menu) {
		if (menuService.update(menu) > 0) {
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
	Tree<Menu> tree() {
		return menuService.getTree();
	}


	@GetMapping("/tree/{roleId}")
	@ResponseBody
	Tree<Menu> tree(@PathVariable("roleId") String roleId) {
		Tree<Menu> tree = menuService.getTree(roleId);
		return tree;
	}

}
