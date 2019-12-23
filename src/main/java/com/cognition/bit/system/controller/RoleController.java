package com.cognition.bit.system.controller;

import com.cognition.bit.system.domain.SysRole;
import com.cognition.bit.system.service.RoleService;
import com.cognition.bit.common.until.ResultData;
import com.cognition.bit.system.persistence.BaseController;
import com.cognition.bit.common.until.Query;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

//    @RequiresPermissions("sys:role:role")
//    @GetMapping("")
//    String role() {
//        return "system/role/role";
//    }

    @GetMapping("/list")
    @ResponseBody
    @RequiresPermissions("sys:role:role")
    List<SysRole> list() {
        Map<String, Object> map = Query.withDelFlag();
        List<SysRole> sysRoleList = roleService.findList(map);
        return sysRoleList;
    }

    //	@Log("添加角色")
    @RequiresPermissions("sys:role:add")
    @GetMapping("/add")
    String add() {
        return "system/role/add";
    }

    //	@Log("编辑角色")
//    @RequiresPermissions("sys:role:edit")
//    @GetMapping("/edit/{id}")
//    String edit(@PathVariable("id") Long id, Model model) {
//        Role role = roleService.get(id);
//        model.addAttribute("role", role);
//        return "system/role/edit";
//    }

    /**
     * 储存角色
     * @param sysRole
     * @return
     */
    //	@Log("保存角色")
    @RequiresPermissions("sys:role:add")
    @PostMapping("/save")
    @ResponseBody
    ResultData save(SysRole sysRole) {
        // 根据权限存入id，更新人
        sysRole.setCreateBy(getUserId());
        if (roleService.save(sysRole) > 0) {
            return ResultData.result(true);
        } else {
            return ResultData.result(false);
        }
    }

    //	@Log("更新角色")
    @RequiresPermissions("sys:role:edit")
    @PostMapping("/update")
    @ResponseBody
    ResultData update(SysRole sysRole) {
        //roleId转值id
        int state = roleService.update(sysRole);
        if (state > 0) {
            return ResultData.result(true);
        } else {
            return ResultData.result(false);
        }
    }

    //	@Log("删除角色")
    @RequiresPermissions("sys:role:remove")
    @PostMapping("/remove")
    @ResponseBody
    ResultData delete(Long id) {
        if (roleService.delete(id) > 0) {
            return ResultData.result(true);
        } else {
            return ResultData.result(false);
        }
    }

    //	@Log("批量删除角色")
    @RequiresPermissions("sys:role:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    ResultData batchRemove(@RequestParam("ids[]") Long[] ids) {
        int r = roleService.batchDelect(ids);
        if (r > 0) {
            return ResultData.result(true);
        }
        return ResultData.result(false);

    }
}
