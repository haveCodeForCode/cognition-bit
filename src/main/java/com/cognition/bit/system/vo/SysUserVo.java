package com.cognition.bit.system.vo;

import com.cognition.bit.system.domain.*;

import java.io.Serializable;
import java.util.List;

/**
 * 用户临时变量
 *
 * @author Worry
 * @version 2019/5/25
 */
public class SysUserVo implements Serializable {


    private Long userId;
    /**
     * 登陆账号
     * <p>
     * userName
     */
    private String userName;

    /**
     * 用户密码
     * <p>
     * userPassword
     */
    private String userPassword;

    /**
     * 用户邮箱
     * <p>
     * userEmail
     */
    private String userEmail;

    /**
     * 手机号
     * <p>
     */
    private String userMobile;

    /**
     * 部门
     * <p>
     * dept
     */
    private Long deptId;

    /**
     * 帐号状态（0正常 1停用）
     * <p>
     * status
     */
    private String status;

    /**
     * 用户昵称
     * <p>
     * name
     */
    private String nickName;

    /**
     * 图片ID
     * <p>
     * picId
     */
    private String avatar;

    /**
     * 上级部门ID，一级部门为0
     */
    private Long parentId;

    /**
     * 部门名称
     */
    private String name;


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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SysRole> getSysRoles() {
        return sysRoles;
    }

    public void setSysRoles(List<SysRole> sysRoles) {
        this.sysRoles = sysRoles;
    }

    public List<SysMenu> getSysMenus() {
        return sysMenus;
    }

    public void setSysMenus(List<SysMenu> sysMenus) {
        this.sysMenus = sysMenus;
    }

    @Override
    public String toString() {
        return "SysUserVo{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userMobile='" + userMobile + '\'' +
                ", deptId=" + deptId +
                ", status='" + status + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", sysRoles=" + sysRoles +
                ", sysMenus=" + sysMenus +
                '}';
    }
}
