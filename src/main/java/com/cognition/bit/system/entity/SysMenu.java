package com.cognition.bit.system.entity;

import com.cognition.bit.system.persistence.BaseEntity;

import java.io.Serializable;

/**
 * 菜单对象
 *
 * @author LineInkBook
 */
public class SysMenu extends BaseEntity<SysMenu> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 父菜单ID，一级菜单为0
     * <p>
     * parentId
     */
    private Long parentId;

    /**
     * 菜单名称
     * <p>
     * name
     */
    private String name;

    /**
     * 菜单URL
     * <p>
     * menuUrl
     */
    private String url;

    /**
     * 类型 bootstrap：目录 1：菜单 2：按钮
     * <p>
     * menuType
     */
    private Integer type;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     * <p>
     * menuPerms
     */
    private String perms;

    /**
     * 菜单图标
     * <p>
     * menuIcon
     */
    private String icon;

    /**
     * 排序
     * <p>
     * orderNum
     */
    private Integer orderNum;


    public SysMenu(){
        super();
    }
    public SysMenu(long id) {
        super(id);
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "parentId='" + parentId + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", type=" + type +
                ", perms='" + perms + '\'' +
                ", icon='" + icon + '\'' +
                ", orderNum=" + orderNum +
                ", id='" + id + '\'' +
                ", createBy='" + createBy + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delFlag='" + delFlag + '\'' +
                '}';
    }
}
