package com.cognition.bit.system.domain;


/**
 * 用户信息
 * @author LineInkBook
 * @version 2018/12/24
 */
public class SysUserInfo {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     * <p>
     * userId
     */
    private Long userId;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    @Override
    public String toString() {
        return "SysUserInfo{" +
                "userId=" + userId +
                ", nickName='" + nickName + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
