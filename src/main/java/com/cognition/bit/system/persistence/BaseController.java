package com.cognition.bit.system.persistence;


import com.cognition.bit.system.config.shiro.ShiroUtils;
import com.cognition.bit.system.entity.SysUser;
import org.springframework.stereotype.Controller;

/**
 * 基础控制层
 * @author taoya
 */
@Controller
public class BaseController {

	public SysUser getUser() {
		return ShiroUtils.getUser();
	}

	public Long getUserId() {
		return getUser().getId();
	}

	public String getUsername() {
		return getUser().getLoginName();
	}

}
