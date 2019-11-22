package com.cognition.bit.system.entity;

import java.io.Serializable;

/**
 * 角色菜单关联表
 * @author Worry
 * @version 2019/3/18
 */
public class SysRoleMenu implements Serializable {

    /**
     * 关联主键
     */
    private Long id;

    /**
     * 角色表主键
     */
    private Long roleId;

    /**
     * 菜单表主键
     */
    private Long menuId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        return "RoleMenu{" +
                "id='" + id + '\'' +
                ", roleId='" + roleId + '\'' +
                ", menuId='" + menuId + '\'' +
                '}';
    }

}
