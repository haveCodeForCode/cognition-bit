package com.cognition.bit.system.config.redis;

import com.cognition.bit.common.config.Constant;
import com.cognition.bit.common.until.SerializeUtil;
import com.cognition.bit.common.until.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Worry
 * @version 2019/7/20
 */
@Service
public class RedisService {

    private RedisManager redisManager;

    @Autowired
    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    /**
     * key取值
     *
     * @param key
     * @return
     */
    public String redisGet(String key) {
        byte[] keyByte = SerializeUtil.serialize(key);
        keyByte = redisManager.redisGet(keyByte);
        if (keyByte!=null && keyByte.length > Constant.INT_ZERO) {
            return Objects.requireNonNull(SerializeUtil.deserialize(keyByte)).toString();
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
        //键值转byte
        byte[] keyByte = SerializeUtil.serialize(key);
        //变量转byte
        byte[] valueByte = SerializeUtil.serialize(value);
        //回传值
        String backValue;
        if (expire > Constant.INT_ZERO) {
            backValue = redisManager.redisSet(keyByte, valueByte, expire);
        } else {
            backValue = redisManager.redisSet(keyByte, valueByte);
        }
        if (StringUtils.isNotEmpty(backValue)) {
            return backValue;
        } else {
            return null;
        }
    }

    /**
     * 删除reids中数据
     *
     * @param key
     */
    public void redisDel(String key) {
        byte[] keyByte = SerializeUtil.serialize(key);
        redisManager.redisDel(keyByte);
    }
}
