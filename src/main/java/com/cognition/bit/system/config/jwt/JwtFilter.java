package com.cognition.bit.system.config.jwt;

import com.cognition.bit.common.until.http.HttpUtil;
import com.cognition.bit.system.config.ApplicationContextRegister;
import com.cognition.bit.system.domain.SysUser;
import com.cognition.bit.system.service.UserService;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT拦截器
 *
 * @author Worry
 * @version 2019/9/27
 */
public class JwtFilter extends BasicHttpAuthenticationFilter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${cognition.jwtLoginSign}")
    private String loginSign;

    /**
     * 检测用户是否登录
     * 检测header里面是否包含Authorization字段即可
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        //登陆标识，配置文件控制
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //登陆标识
        String authorization = httpServletRequest.getHeader(loginSign);
        return authorization != null;
    }

    /**
     * 登录验证
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //登陆标识
        String header = httpServletRequest.getHeader(loginSign);
        JwtToken token = new JwtToken(header);
        getSubject(request, response).login(token);
        return true;
    }

    /**
     * 刷新token时间
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response);
            } catch (Exception e) {
                logger.info(e.getMessage());
                //判断如果抛出token失效，则执行刷新token逻辑
                if (e.getMessage().contains("expired")) {
                    //获取用户信息，配置文件控制
                    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
                    String oldToken = httpServletRequest.getHeader(loginSign);
                    Long userId = JwtUtil.getUserId(oldToken);
                    UserService userService = ApplicationContextRegister.getBean(UserService.class);
                    SysUser user = userService.get(userId);
                    //验证refreshToken是否有效
//                    if (userService.refreshTokenIsValid(oldToken)) {
//                        //生成新token 返回界面
//                        String newToken = JwtUtil.sign(user.getId(), user.getUserPassword());
//                        JwtToken jwtToken = new JwtToken(newToken);
//                        this.getSubject(request, response).login(jwtToken);
//                        HttpUtil.getResponse().setHeader("token", newToken);
//                        return true;
//                    }
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 对跨域提供支持
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}
