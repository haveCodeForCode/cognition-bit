package com.cognition.bit.system.domain;

/**
 * 用户角色关联表
 * @author taoya
 */
public class SysUserRole {
    /**
     * 用户主键
     * <p>
     * userId
     */
    private Long userId;
    /**
     * 角色主键
     * <p>
     *  roleId
     */
    private Long roleId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "UserRoleDO{" +
                ", userId=" + userId +
                ", roleId=" + roleId +
                '}';
    }
}
