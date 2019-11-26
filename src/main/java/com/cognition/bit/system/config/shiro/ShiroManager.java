package com.cognition.bit.system.config.shiro;

//slf4j日志

import com.cognition.bit.system.domain.SysUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//设置注解


/**
 * Shiro 操作（工具）类
 *
 * @author LineInkBook
 */
public class ShiroManager {
    /**
     * 获取Shiro日志
     */
    private static final Logger shiroLogger = LoggerFactory.getLogger(ShiroManager.class);

    /**
     * Shiro获取对象
     */
    private static Subject getSecuritySubject() {
        return SecurityUtils.getSubject();
    }

    /**
     * 通过session获取userid
     *
     * @return
     */
    public static Long getUserId() {
        SysUser sysUser = (SysUser) ShiroManager.getSessionObject();
        return sysUser.getId();
    }

    /**
     * 获取登陆内的ObeJect
     *
     * @return
     */
    private static Object getSessionObject() {
        Object object = getSecuritySubject().getPrincipal();
        return object;
    }

    /**
     * 登出
     */
    public static void logout() {
        getSecuritySubject().logout();
    }

}
