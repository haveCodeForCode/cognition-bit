/**
 *
 */
package com.cognition.bit.system.persistence;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.cognition.bit.common.config.Constant;
import com.cognition.bit.common.until.codegenerate.SnowFlake;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;


/**
 * Entity支持类
 * @author warry
 * @version 2014-05-16
 */
public abstract class BaseEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 实体编号（唯一标识）
     *
     * @ id
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
     * 查询标记
     * <p>
     * searchinfo
     */
    protected String searchInfo;

    /**
     * 自定义SQL（SQL标识，SQL内容）
     *
     * @ sqlMap
     */
    protected Map<String, String> sqlMap;

    public BaseEntity(long id) {
    }

    public BaseEntity() {
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

    public String getSearchInfo() {
        return searchInfo;
    }

    public void setSearchInfo(String searchInfo) {
        this.searchInfo = searchInfo;
    }

    @Length(min = 1, max = 1)
    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }


//    /**
//     * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
//     * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
//     */
//    protected boolean isNewRecord = false;

//	@JsonIgnore
//	@XmlTransient
//	public Map<String, String> getSqlMap() {
//		if (sqlMap == null){
//			sqlMap = Maps.newHashMap();
//		}
//		return sqlMap;
//	}

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
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
