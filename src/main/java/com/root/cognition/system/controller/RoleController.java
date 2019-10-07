package com.root.cognition.system.controller;

import com.root.cognition.common.until.ResultData;
import com.root.cognition.system.persistence.BaseController;
import com.root.cognition.common.until.Query;
import com.root.cognition.system.entity.Role;
import com.root.cognition.system.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 角色控制层
 * @author taoya
 */
@RequestMapping("/system/role")
@Controller
public class RoleController extends BaseController {

    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @RequiresPermissions("sys:role:role")
    @GetMapping("")
    String role() {
        return "system/role/role";
    }

    @GetMapping("/list")
    @ResponseBody
    @RequiresPermissions("sys:role:role")
    List<Role> list() {
        Map<String, Object> map = Query.withDelFlag();
        List<Role> roleList = roleService.findList(map);
        return roleList;
    }

    //	@Log("添加角色")
    @RequiresPermissions("sys:role:add")
    @GetMapping("/add")
    String add() {
        return "system/role/add";
    }

    //	@Log("编辑角色")
    @RequiresPermissions("sys:role:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Long id, Model model) {
        Role role = roleService.get(id);
        model.addAttribute("role", role);
        return "system/role/edit";
    }

    //	@Log("保存角色")
    @RequiresPermissions("sys:role:add")
    @PostMapping("/save")
    @ResponseBody
    ResultData save(Role role) {
        // 根据权限存入id，更新人
        role.setCreateBy(getUserId());
        if (roleService.save(role) > 0) {
            return ResultData.success();
        } else {
            return ResultData.error();
        }
    }

    //	@Log("更新角色")
    @RequiresPermissions("sys:role:edit")
    @PostMapping("/update")
    @ResponseBody
    ResultData update(Role role) {
        //roleId转值id
        int state = roleService.update(role);
        if (state > 0) {
            return ResultData.success();
        } else {
            return ResultData.error();
        }
    }

    //	@Log("删除角色")
    @RequiresPermissions("sys:role:remove")
    @PostMapping("/remove")
    @ResponseBody
    ResultData delete(Long id) {
        if (roleService.delete(id) > 0) {
            return ResultData.success();
        } else {
            return ResultData.error();
        }
    }

    //	@Log("批量删除角色")
    @RequiresPermissions("sys:role:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    ResultData batchRemove(@RequestParam("ids[]") Long[] ids) {
        int r = roleService.batchDelect(ids);
        if (r > 0) {
            return ResultData.success();
        }
        return ResultData.error();

    }
}
