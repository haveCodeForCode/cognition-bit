/**
 *
 */
package com.cognition.bit.system.persistence;

import com.cognition.bit.common.config.Constant;
import com.cognition.bit.common.until.codegenerate.SnowFlake;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;


/**
 * Entity支持类
 * @author warry
 * @version 2014-05-16
 */
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 实体类id（编号）
     * <p>
     * id
     */
    protected Long id;

    /**
     * 创建者
     * <p>
     * createBy
     */
    protected Long createBy;

    /**
     * 更新者
     * <p>
     * updateBy
     */
    protected Long updateBy;

    /**
     * 创建日期
     * <p>
     * gmtCreate
     */
    protected Date createTime;

    /**
     * 更新日期
     * <p>
     * UpdateTime
     */
    protected Date updateTime;

    /**
     * 删除标记（bootstrap：正常；1：删除；2：审核）
     * <p>
     * delFlag
     */
    protected String delFlag;


    /**
     * 备注
     * <p>
     * remark
     */
    private String remark;

    /**
     * 数据权限
     * <p>
     *  dataScope
     */
    private String dataScope;

    /**
     * 请求参数
     * 查询标记
     * <p>
     * params
     */
    @JsonIgnore
    private String params;

    /**
     * 搜索值
     */
    private String searchValue;

    public BaseEntity() {
    }

    public BaseEntity(long id) {
    }

    @JsonSerialize(using = ToStringSerializer.class)
    public Long getId() {
        return id;
    }

    /**
     * long从后台传到前台会精度不准确，
     * 续将其转换成String向前台赋值
     *
     * @param id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Length(min = 1, max = 1)
    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    /**
     * 预插入数据补充
     */
    public void preInsert() {
        // 不限制ID为UUID，调用setIsNewRecord()使用自定义ID
        Long sfUserID = SnowFlake.createSFid();
        setId(sfUserID);
        this.updateBy = sfUserID;
        this.createBy = sfUserID;
        this.createTime = new Date();
        this.updateTime = createTime;
        this.delFlag = Constant.DEL_FLAG_NORMAL;
    }

}
