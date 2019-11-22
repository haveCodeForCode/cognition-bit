package com.cognition.bit.system.config.redis;


import com.cognition.bit.common.until.SerializeUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * 通过redis方法实现(重写)Cache缓存方法同时加密
 *  RedisManager(redis底层实现）->RedisCache(redis缓存实现）
 * @author worry
 */
public class RedisCache<K, V> implements Cache<K, V> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 初始化redisManager实例
     */
    private RedisManager cache;

    /**
     * redis的会话密钥
     */
    private String keyPrefix = "shiroSession_redisCache:";

    /**
     * 通过一个JedisManager实例构造RedisCache
     * @param cache 缓存管理器实例
     */
    private RedisCache(RedisManager cache) {
        if (cache == null) {
            throw new IllegalArgumentException("Cache argument cannot be null.");
        }
        this.cache = cache;
    }

    /**
     * 使用指定的Redis管理器加上密钥前缀。
     *
     * @param cache  缓存管理器实例
     * @param prefix 获取redis密钥前缀
     */
    public RedisCache(RedisManager cache, String prefix) {
        //获取当前缓存对象为this
        this(cache);
        // set the prefix
        this.keyPrefix = prefix;
    }

    /**
     * key获取cache中对象（重写cache获取方法）
     *
     * @param key               所获取对象的key值
     * @return value            redis中key所对应的对象
     * @throws CacheException   抛出异常
     */
    @Override
    public V get(K key) throws CacheException {
        logger.debug("根据key从Redis中获取对象 key [" + key + "]");
        try {
            if (key == null) {
                return null;
            } else {
                byte[] rawValue = cache.redisGet(getByteKey(key));
                //反序列化
                @SuppressWarnings("unchecked")
                V value = (V) SerializeUtil.deserialize(rawValue);
                return value;
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    /**
     *  key值存入（重写cache存入方法）
     *
     * @param key     存入对象的key值
     * @param value   存入对象的value值
     * @return        返回变量
     * @throws CacheException 抛出异常
     */
    @Override
    public V put(K key, V value) throws CacheException {
        logger.debug("根据key从存储 key [" + key + "]");
        try {
            cache.redisSet(getByteKey(key), SerializeUtil.serialize(value));
            return value;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    /**
     * key值删除（重写cache删除方法）
     *
     * @param key  键值
     * @throws CacheException 抛出异常
     */
    @Override
    public V remove(K key) throws CacheException {
        logger.debug("从redis中删除 key [" + key + "]");
        try {
            V previous = get(key);
            cache.redisDel(getByteKey(key));
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    /**
     * 清空redis缓存（重写cache删除方法）
     */
    @Override
    public void clear() throws CacheException {
        logger.debug("从redis中删除所有元素");
        try {
            cache.redisFlushDB();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    /**
     * 测试缓存大小
     * redisDbSize
     * @return
     */
    @Override
    public int size() {
        try {
            Long longSize = cache.redisDbSize();
            return longSize.intValue();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    /**
     * 钥匙存入缓存
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keys() {
        try {
            Set<byte[]> keys = cache.redisKeys(this.keyPrefix + "*");
            if (CollectionUtils.isEmpty(keys)) {
                return Collections.emptySet();
            } else {
                Set<K> newKeys = new HashSet<K>();
                for (byte[] key : keys) {
                    newKeys.add((K) key);
                }
                return newKeys;
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    /**
     * 给想获得byte[]型的key加上密匙
     *
     * @param key
     * @return
     */
    private byte[] getByteKey(K key) {
        if (key instanceof String) {
            String preKey = this.keyPrefix + key;
            return preKey.getBytes();
        } else {
            return SerializeUtil.serialize(key);
        }
    }

    @Override
    public Collection<V> values() {
        try {
            Set<byte[]> keys = cache.redisKeys(this.keyPrefix + "*");
            if (!CollectionUtils.isEmpty(keys)) {
                List<V> values = new ArrayList<V>(keys.size());
                for (byte[] key : keys) {
                    @SuppressWarnings("unchecked")
                    V value = get((K) key);
                    if (value != null) {
                        values.add(value);
                    }
                }
                return Collections.unmodifiableList(values);
            } else {
                return Collections.emptyList();
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

}
