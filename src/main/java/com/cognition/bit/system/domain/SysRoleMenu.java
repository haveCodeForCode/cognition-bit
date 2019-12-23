package com.cognition.bit.system.domain;

import java.io.Serializable;

/**
 * 角色菜单关联表
 * @author Worry
 * @version 2019/3/18
 */
public class SysRoleMenu implements Serializable {

    /**
     * 角色表主键
     */
    private Long roleId;

    /**
     * 菜单表主键
     */
    private Long menuId;


    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    @Override
    public String toString() {
        return "SysRoleMenu{" +
                "roleId=" + roleId +
                ", menuId=" + menuId +
                '}';
    }

}
