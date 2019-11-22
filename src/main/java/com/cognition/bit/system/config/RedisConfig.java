package com.cognition.bit.system.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author LineInkBook
 */
@Configuration
public class RedisConfig {

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
     * jedispool数据库变量
     */
    private static JedisPool jedisPool = null;

    /**
     * 初始化方法
     * 通过地址（redisHost）端口号（redisPort）和密码（redisPassword）连接时间（redisTimeout）设置
     */
    @Bean("redisPoolFactory")
    public JedisPool redisPoolFactory() {
        if (jedisPool == null) {
            if (redisPassword != null && !"".equals(redisPassword)) {
                jedisPool = new JedisPool(new JedisPoolConfig(), redisHost, redisPort, redisTimeout, redisPassword);
            } else if (redisTimeout != 0) {
                jedisPool = new JedisPool(new JedisPoolConfig(), redisHost, redisPort, redisTimeout);
            } else {
                jedisPool = new JedisPool(new JedisPoolConfig(), redisHost, redisPort);
            }
        }
        return jedisPool;
    }

    /**
     * 初始化jedis方法
     *
     * @return
     */
    @Bean("redisJedis")
    @DependsOn("redisPoolFactory")
    public Jedis redisJedis() {
        return jedisPool.getResource();
    }


}
