package com.root.cognition.system.config;


import com.root.cognition.system.config.redis.RedisCacheManager;
import com.root.cognition.system.config.redis.RedisManager;
import com.root.cognition.system.config.shiro.ShiroDatabaseRealm;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * 系统基本设置
 *
 * @author LineInkBook
 */
@Configuration
public class SystemConfig {

    /**
     * 使用类型缓存
     */
    @Value("${spring.cache.type}")
    private String cacheType;

    /**
     * 关闭shiro认证控制器
     * @return
     */
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


//*************************cacheManager缓存******************************

    /**
     * ehcache缓存初始化设置
     *
     * @return
     */
    @Bean("ehCacheManager")
    public EhCacheManager ehCacheManager() {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManager(cacheManager());
        return ehCacheManager;
    }

    /**
     * 创建EhCache，创建驱动
     *
     * @return
     */
    @Bean("cacheManagerSet")
    public CacheManager cacheManager() {
        return CacheManager.create();
    }


    /**
     * redis管理器预设
     *
     * @return
     */
    @Bean("redisManager")
    RedisManager redisManager() {
        return new RedisManager();
    }

    /**
     * cacheManager 缓存 （redis实现）
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean("rediscacheManager")
    RedisCacheManager rediscacheManager() {
        //初始化redis缓存对象
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        //将redis对象放入操作对象返回，完成redis初始化
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }


    //*********************************spring代理*****************************************

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


}
