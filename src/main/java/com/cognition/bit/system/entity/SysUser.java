package com.cognition.bit.system.entity;

import com.cognition.bit.system.persistence.BaseEntity;

/**
 * 用户Entity
 * @author LineInkBook
 * @version 2018/12/24
 */
public class SysUser extends BaseEntity<SysUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 登陆账号名
     * <p>
     * userName
     */
    private String loginName;

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

    public SysUser(long id) {
        super(id);
    }

    public SysUser() {
        super();
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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

    @Override
    public String toString() {
        return "User{" +
                "loginName='" + loginName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userMobile='" + userMobile + '\'' +
                ", deptId=" + deptId +
                ", id=" + id +
                ", createBy=" + createBy +
                ", updateBy=" + updateBy +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag='" + delFlag + '\'' +
                '}';
    }
}
