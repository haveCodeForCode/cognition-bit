package com.cognition.bit.system.config.shiro;


import com.cognition.bit.system.service.UserService;
import com.google.common.collect.Maps;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro 设置
 * Cache 缓存设置
 *
 * @author LineInkBook
 */
@Configuration
public class ShiroConfig {


    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.password}")
    private String redisPassword;
    @Value("${spring.redis.port}")
    private int redisPort;
    /**
     * 时间内没有连接上就断开连接
     */
    @Value("${spring.redis.timeout}")
    private int redisTimeout;

    /**
     * Shiro拦截器配置
     *
     * @param securityManager
     * @return
     */
    @Bean(name = "shiroFilterFactory")
    ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager, UserService userService) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //登录地址
        shiroFilterFactoryBean.setLoginUrl("/toInterface");
        shiroFilterFactoryBean.setSuccessUrl("/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        //退出拦截器
        Map<String, Filter> filterMap = Maps.newHashMap();
        filterMap.put("logout", new ShiroLogoutFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/toLogin", "anon");
        filterChainDefinitionMap.put("/toHome", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/user/info", "anon");
        filterChainDefinitionMap.put("/website-front/**", "anon");
        filterChainDefinitionMap.put("/logout", "logout");
//        filterChainDefinitionMap.put("/toInterface", "anon");
//        filterChainDefinitionMap.put("/toRegister", "anon");
//        filterChainDefinitionMap.put("/getVerify", "anon");
//        filterChainDefinitionMap.put("/docs/**", "anon");
//        filterChainDefinitionMap.put("/druid/**", "anon");
//        filterChainDefinitionMap.put("/upload/**", "anon");
//        filterChainDefinitionMap.put("/files/**", "anon");
//        filterChainDefinitionMap.put("/blog", "anon");
//        filterChainDefinitionMap.put("/blog/open/**", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }


    /**
     * 添加shiro 自主注入bean的生命周期管理
     *
     * @return
     */
    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    //*********************************认证管理*****************************************

    /**
     * 认证管理器 自定义认证管理办法
     *
     * @return
     */
    @Bean("securityManager")
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(new ShiroAuthorizingRealm());
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(redisCacheManager());
        return securityManager;
    }


    /**
     * 自定义sessionManager
     *
     * @return
     */
    @Bean
    public SessionManager sessionManager() {
        ShiroSessionManager sessionManager = new ShiroSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }

    /**
     * RedisSessionDAO
     * shiro sessionDao层的实现 通过redis
     *
     * @return
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setExpire(1800);
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }
//****************************cacheManager缓存******************************
    /**
     * cacheManager 缓存 redis实现
     * 通过spring注入cacheManager
     *
     * @return
     */
    @Bean
    public RedisCacheManager redisCacheManager() {
        //通过spring.data.redis配置
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

//**********ehcache

    /**
     * 创建EhCache，创建驱动
     * EhCache 是一个纯Java的进程内缓存框架，直接引用
     *
     * @return
     */
    @Bean("eCacheManager")
    public CacheManager eCacheManager() {
        return CacheManager.create();
    }

    /**
     * ehcache缓存初始化设置
     *
     * @return
     */
    @Bean("ehCacheManager")
    public EhCacheManager ehCacheManager() {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManager(eCacheManager());
        return ehCacheManager;
    }

//***********************************************************************************

    /**
     * 配置shiro redisManager
     * <p>
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisHost);
        redisManager.setPort(redisPort);
        redisManager.setDatabase(9);
        redisManager.setTimeout(redisTimeout);
        redisManager.setPassword(redisPassword);
        return redisManager;
    }


    /**
     * spring自动代理
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * 关闭shiro认证控制器,使用jwt
     * @return
     @Bean("securityManager")
     public SecurityManager securityManager() {
     DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
     //设置realm.
     securityManager.setRealm(new ShiroDatabaseRealm());
     //关闭自带session
     DefaultSessionStorageEvaluator evaluator = new DefaultSessionStorageEvaluator();
     //session储存关闭
     evaluator.setSessionStorageEnabled(false);
     //        是否使用Subject's Session来保持其自身的状态，根据配置，在每个对象的基础上进行控制sessionStorageEvaluator。
     //        默认Evaluator值为DefaultSessionStorageEvaluator，它支持在全局级别为所有主题启用或禁用会话持久性会话使用（默认情况下允许使用会话）。
     DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
     //全局关闭session
     subjectDAO.setSessionStorageEvaluator(evaluator);
     //
     securityManager.setSubjectDAO(subjectDAO);
     return securityManager;
     }
     *
     */

    /**
     * 凭证匹配器
     * MD5加密
     */
//    @Bean
//    public HashedCredentialsMatcher hashedCredentialsMatcher() {
//        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//        //散列算法:这里使用MD5算法;
//        hashedCredentialsMatcher.setHashAlgorithmName("md5");
//        //散列的次数，比如散列两次，相当于 md5(md5(""));
//        hashedCredentialsMatcher.setHashIterations(2);
//        return hashedCredentialsMatcher;
//    }

}

