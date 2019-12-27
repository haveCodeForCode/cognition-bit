package com.cognition.bit.system.config.shiro;

import com.cognition.bit.common.config.Constant;
import com.cognition.bit.common.until.StringUtils;
import com.cognition.bit.common.until.encrypt.Md5Utils;
import com.cognition.bit.system.config.ApplicationContextRegister;
import com.cognition.bit.system.dao.UserDao;
import com.cognition.bit.system.domain.SysUser;
import com.cognition.bit.system.service.MenuService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

//import com.cognition.bit.system.config.jwt.JwtUtil;

/**
 * @author 1122
 * @version 2019/9/28
 */
public class ShiroAuthorizingRealm extends AuthorizingRealm {

    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
        Long userId = ShiroUtils.getUserId();
        MenuService menuService = ApplicationContextRegister.getBean(MenuService.class);
        Set<String> perms = menuService.menuListPerms(userId);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(perms);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String loginInfo =  token.getPrincipal().toString();
        String password = new String((char[]) token.getCredentials());

        UserDao userDao = ApplicationContextRegister.getBean(UserDao.class);
        Long userId =  Long.parseLong(loginInfo);
        // 查询用户信息
        SysUser user = userDao.get(userId);

        // 账号不存在
        if (user == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }

        String encPassword = Md5Utils.encrypt(userId.toString(), password);

        // 密码错误
        if (!encPassword.equals(user.getUserPassword())) {
            throw new IncorrectCredentialsException("账号或密码不正确");
        }

        // 账号锁定
        if (Constant.STRING_ONE.equals(user.getDelFlag())) {
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }
        return new SimpleAuthenticationInfo(user, password, getName());
    }


    /**
     * 授权身份信息
     *
     * @param principalCollection
     * @return
     */
//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//        //新建授权配置
//        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        logger.info("授权身份信息：" + principalCollection.toString());
//        //菜单服务类注册
//        MenuService menuService = ApplicationContextRegister.getBean(MenuService.class);
//        //获取userID
//        SysUser sysUser = (SysUser) principalCollection.getPrimaryPrincipal();
//        //判断用户对象是否为空
//        if (sysUser != null) {
//            Set<String> perms = menuService.menuListPerms(sysUser.getId());
//            info.setStringPermissions(perms);
//        }
//        return info;
//    }

    /**
     * 验证身份信息
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        //获取用户的输入的账号
//        String token = authenticationToken.getPrincipal().toString();
//        String password = new String((char[]) authenticationToken.getCredentials());
//
//        UserDao userDao = ApplicationContextRegister.getBean(UserDao.class);
//        if (StringUtils.isNotEmpty(token)) {
//            SysUser sysUser = userDao.getWihtLogininfo(token);
//            if (sysUser !=null){
//                if (sysUser.getDelFlag().equals(Constant.STRING_ONE)) {
//                    throw new DisabledAccountException("901");
//                }
//                String encPassword = Md5Utils.encrypt(sysUser.getId().toString(), password);
//                return new SimpleAuthenticationInfo(sysUser, encPassword, getName());
//            }else {
//                throw new UnknownAccountException("900");
//            }
//        }else{
//            throw new UnknownAccountException("900");
//        }
//    }

//    /**
//     * 替换原本shiro token
//     *
//     * @param token
//     * @return
//     */
//    @Override
//    public boolean supports(AuthenticationToken token) {
//        //JWTtoken
//        return token instanceof JwtToken;
//        //        return super.supports(token);
//    }


}
