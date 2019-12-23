package com.cognition.bit.framework.entity;

import com.cognition.bit.system.persistence.BaseEntity;

import java.io.Serializable;

/**
 * 文件上传
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-19 16:02:20
 */
public class ModFileRecord extends BaseEntity implements Serializable {

    /***文件名称*/
    private String fileName;
    /*** 文件类型*/
    private Integer type;
    /***  URL地址*/
    private String url;

    public ModFileRecord(){
        super();
    }

    public ModFileRecord(String fileName, Integer type, String url) {
        super();
        this.fileName = fileName;
        this.type = type;
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
