package com.cognition.bit.modules.entity;


import com.cognition.bit.system.persistence.BaseEntity;

import java.io.Serializable;


/**
 * 短息日志表
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2019-06-15 15:54:43
 */
public class ModSmsLog extends BaseEntity<ModSmsLog> implements Serializable {
	private static final long serialVersionUID = 1L;


	/**
	 * 模块
	 */
	private String module;
	/**
	 * 手机号码
	 */
	private String mobile;
	/**
	 * 短信内容
	 */
	private String content;
	/**
	 * 是否推送（0草稿；1：发送成功
	 */
	private String ispush;
	/**
	 * 备注
	 */
	private String remake;
	/**
	 * 模版ID
	 */
	private String templateCode;

	/*** 短信回传状态值*/
	private String smsReturnCode;

	/*** 短信回传状态信息*/
	private String smsReturnMessage;


	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIspush() {
		return ispush;
	}

	public void setIspush(String ispush) {
		this.ispush = ispush;
	}

	public String getRemake() {
		return remake;
	}

	public void setRemake(String remake) {
		this.remake = remake;
	}

	public String getSmsReturnCode() {
		return smsReturnCode;
	}

	public void setSmsReturnCode(String smsReturnCode) {
		this.smsReturnCode = smsReturnCode;
	}

	public String getSmsReturnMessage() {
		return smsReturnMessage;
	}

	public void setSmsReturnMessage(String smsReturnMessage) {
		this.smsReturnMessage = smsReturnMessage;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	@Override
	public String toString() {
		return "SmsLog{" +
				"module='" + module + '\'' +
				", mobile='" + mobile + '\'' +
				", content='" + content + '\'' +
				", ispush='" + ispush + '\'' +
				", remake='" + remake + '\'' +
				", templateCode='" + templateCode + '\'' +
				", smsReturnCode='" + smsReturnCode + '\'' +
				", smsReturnMessage='" + smsReturnMessage + '\'' +
				", id=" + id +
				", createBy=" + createBy +
				", updateBy=" + updateBy +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", delFlag='" + delFlag + '\'' +
				'}';
	}
}
