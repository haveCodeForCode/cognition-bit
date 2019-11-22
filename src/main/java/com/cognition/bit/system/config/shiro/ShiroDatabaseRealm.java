package com.cognition.bit.system.config.shiro;

import com.cognition.bit.common.config.Constant;
import com.cognition.bit.common.until.StringUtils;
import com.cognition.bit.system.config.ApplicationContextRegister;
import com.cognition.bit.system.config.jwt.JwtToken;
import com.cognition.bit.system.config.jwt.JwtUtil;
import com.cognition.bit.system.dao.UserDao;
import com.cognition.bit.system.entity.SysUser;
import com.cognition.bit.system.service.MenuService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @author 1122
 * @version 2019/9/28
 */
public class ShiroDatabaseRealm extends AuthorizingRealm {

    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 授权身份信息
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        logger.info("授权身份信息：" + principalCollection.toString());
        //菜单服务类注册
        MenuService menuService = ApplicationContextRegister.getBean(MenuService.class);
        //获取userID
        String userId = JwtUtil.getUserId(principalCollection.toString());
        //新建授权
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //判断用户对象是否为空
        if (StringUtils.isNotEmpty(userId)) {
            Set<String> perms = menuService.menuListPerms(Long.valueOf(userId));
            info.setStringPermissions(perms);
        }
        return info;
    }

    /**
     * 替换原本shiro token
     *
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        //JWTtoken
        return token instanceof JwtToken;
        //        return super.supports(token);
    }

    /**
     * 验证身份信息
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = authenticationToken.getCredentials().toString();
        String userId = JwtUtil.getUserId(token);
        UserDao userDao = ApplicationContextRegister.getBean(UserDao.class);
        if (StringUtils.isNotEmpty(userId)){
            SysUser sysUser = userDao.get(Long.valueOf(userId));
            if (sysUser !=null){
                if (sysUser.getDelFlag().equals(Constant.STRING_ZERO)){
                    throw new DisabledAccountException("901");
                }
                if(!JwtUtil.verify(token,userId, sysUser.getUserPassword())){
                    throw new UnknownAccountException("900");
                }
                return new SimpleAuthenticationInfo(token, token, "realm");
            }else {
                throw new UnknownAccountException("900");
            }
        }else{
            throw new UnknownAccountException("900");
        }
    }
}
