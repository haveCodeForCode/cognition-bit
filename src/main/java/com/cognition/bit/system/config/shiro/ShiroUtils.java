package com.cognition.bit.system.config.shiro;

import com.cognition.bit.system.domain.SysUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

/**
 * @author taoya
 */
public class ShiroUtils {


    private static SessionDAO sessionDAO;

    @Autowired
    public static void setSessionDAO(SessionDAO sessionDAO) {
        ShiroUtils.sessionDAO = sessionDAO;
    }

    private static Subject getSubjct() {
        return SecurityUtils.getSubject();
    }

    public static SysUser getUser() {
        Object object = getSubjct().getPrincipal();
        return (SysUser) object;
    }

    public static Long getUserId() {
        return getUser().getId();
    }

    public static void logout() {
        getSubjct().logout();
    }

    public static List<Principal> getPrinciples() {
        List<Principal> principals = null;
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        return principals;
    }
}
