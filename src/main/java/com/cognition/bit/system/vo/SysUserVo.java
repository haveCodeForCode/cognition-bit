package com.cognition.bit.system.vo;

import com.cognition.bit.system.entity.*;

import java.io.Serializable;
import java.util.List;

/**
 * 用户临时变量
 *
 * @author Worry
 * @version 2019/5/25
 */
public class SysUserVo implements Serializable {

    /**
     * 用户账户信息
     */
    private SysUser sysUser;
    /**
     * 用户基本信息
     * <p>
     * userInfo
     */
    private SysUserInfo sysUserInfo;

    /**
     * 部门信息
     * <p>
     * dept
     */
    private SysDept sysDept;

    /**
     * 用户角色对象
     * <p>
     * roleIds
     */
    private List<SysRole> sysRoles;

    /**
     * 菜单树
     */
    private List<SysMenu> sysMenus;

    /**
     * 用户相关登陆信息
     */
    private String mobile;

    /**
     * 登陆密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    public List<SysMenu> getSysMenus() {
        return sysMenus;
    }

    public void setSysMenus(List<SysMenu> sysMenus) {
        this.sysMenus = sysMenus;
    }

    public SysUserInfo getSysUserInfo() {
        return sysUserInfo;
    }

    public void setSysUserInfo(SysUserInfo sysUserInfo) {
        this.sysUserInfo = sysUserInfo;
    }

    public SysDept getSysDept() {
        return sysDept;
    }

    public void setSysDept(SysDept sysDept) {
        this.sysDept = sysDept;
    }

    public List<SysRole> getSysRoles() {
        return sysRoles;
    }

    public void setSysRoles(List<SysRole> sysRoles) {
        this.sysRoles = sysRoles;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserVo{" +
                "user=" + sysUser +
                ", userInfo=" + sysUserInfo +
                ", dept=" + sysDept +
                ", roles=" + sysRoles +
                ", menus=" + sysMenus +
                ", mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
