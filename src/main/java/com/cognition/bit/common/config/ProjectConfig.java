package com.cognition.bit.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Worry
 * @version 2019/9/13
 */
@Component
@ConfigurationProperties(prefix="cognition")
public class ProjectConfig {

    /*** 上传路径*/
    private String uploadPath;
    /*** jwt时间即时*/
    private Integer jwtTime;
    /*** jwt登陆标识*/
    private String jwtLoginSign;

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public Integer getJwtTime() {
        return jwtTime;
    }

    public void setJwtTime(Integer jwtTime) {
        this.jwtTime = jwtTime;
    }

    public String getJwtLoginSign() {
        return jwtLoginSign;
    }

    public void setJwtLoginSign(String jwtLoginSign) {
        this.jwtLoginSign = jwtLoginSign;
    }
}
