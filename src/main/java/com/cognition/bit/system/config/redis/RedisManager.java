package com.cognition.bit.system.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * redis 连接池、直接操作reids方法
 *
 * @author worry
 */
public class RedisManager {

    private JedisPool jedisPool;

    private Jedis jedis;


    @Autowired
    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Autowired
    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    /**
     * 初始化截止redis终端时间
     */
    private int expire = 1;

    /**
     * 利用key从redis中获得value
     *
     * @param key
     * @return
     */
    byte[] redisGet(byte[] key) {
        byte[] value = null;
        value = jedis.get(key);
        return value;
    }

    /**
     * key ，value放入redis
     *
     * @param key
     * @param value
     * @return
     */
    String redisSet(byte[] key, byte[] value) {
        String backValue;
        backValue = jedis.set(key, value);
        return backValue;
    }

    /**
     * key,value,expire（存在时间）放入redis
     *
     * @param key
     * @param value
     * @param expire
     * @return
     */
    String redisSet(byte[] key, byte[] value, int expire) {
        String backValue;
        backValue = jedis.set(key, value);
        if (expire != 0) {
            Long tiems = jedis.expire(key, expire);
            backValue = backValue + tiems.toString();
        }
        return backValue;
    }

    /**
     * 根据key删除redis
     *
     * @param key
     */
    void redisDel(byte[] key) {
        jedis.del(key);
    }

    /**
     * 清空reids
     */
    void redisFlushDB() {
        jedis.flushDB();

    }

    /**
     * 获取缓存池中数量
     */
    Long redisDbSize() {
        Long dbSize = 0L;
        dbSize = jedis.dbSize();
        return dbSize;
    }

    /**
     * keys
     *
     * @param pattern
     * @return
     */
    Set<byte[]> redisKeys(String pattern) {
        Set<byte[]> keys = null;
        keys = jedis.keys(pattern.getBytes());
        return keys;
    }

    int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }
}
