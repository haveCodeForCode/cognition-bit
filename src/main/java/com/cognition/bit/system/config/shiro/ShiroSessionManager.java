package com.cognition.bit.system.config.shiro;

import com.cognition.bit.common.until.StringUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * 自定义sessionId获取
 *
 * @author Worry
 * @version 2019/12/24
 */
public class ShiroSessionManager extends DefaultWebSessionManager {

    /**
     * 授权常量名
     */
    private static final String AUTHORIZATION = "Authorization";

    /**
     * 无状态请求
     */
    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";


    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        //shiro获取http头
        String session_id = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
        //如果请求头中有 Authorization 则其值为sessionId
        if (!StringUtils.isEmpty(session_id)){
            //存放sessionId
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID,session_id);
            //cookieId,存放cookieId
            request.setAttribute(ShiroHttpServletRequest.COOKIE_SESSION_ID_SOURCE,REFERENCED_SESSION_ID_SOURCE);
            //使存放是否引用
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID,Boolean.TRUE);
            return session_id;
        }else {
            //否则按默认规则从cookie取sessionId
            return super.getSessionId(request, response);
        }
    }
}
