package com.cognition.bit.system.config.redis;

import com.cognition.bit.common.until.SerializeUtil;
import com.cognition.bit.common.until.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author Worry
 * @version 2019/7/20
 */
@Service
public class RedisService {

    private RedisCache redisCache;

    @Autowired
    public void setRedisCache(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    /**
     * key取值
     *
     * @param key
     * @return
     */
    public String redisGet(String key) {
        String captcha = redisCache.getCacheObject(key);
        if (StringUtils.isNotEmpty(captcha)) {
            return captcha;
        } else {
            return null;
        }
    }

    /**
     * key、value存放
     *
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public String redisSet(String key, String value, int expire) {
        //回传值
        redisCache.setCacheObject(key, value, expire, TimeUnit.MINUTES);
        return null;
    }

    /**
     * 删除reids中数据
     *
     * @param key
     */
    public void redisDel(String key) {
        redisCache.deleteObject(key);
    }
}
