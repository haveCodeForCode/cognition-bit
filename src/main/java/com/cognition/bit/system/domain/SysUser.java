package com.cognition.bit.system.domain;

import com.cognition.bit.system.persistence.BaseEntity;

import java.util.Date;

/**
 * 用户Entity
 * @author LineInkBook
 * @version 2018/12/24
 */
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

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
     * 最后登陆IP
     * <p>
     * loginIp
     */
    private String loginIp;

    /**
     * 最后登陆时间
     * <p>
     * loginDate
     */
    private Date loginDate;

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
        return "SysUser{" +
                "userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userMobile='" + userMobile + '\'' +
                ", deptId=" + deptId +
                ", status='" + status + '\'' +
                ", loginIp='" + loginIp + '\'' +
                ", loginDate=" + loginDate +
                ", id=" + id +
                ", createBy=" + createBy +
                ", updateBy=" + updateBy +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag='" + delFlag + '\'' +
                '}';
    }
}
