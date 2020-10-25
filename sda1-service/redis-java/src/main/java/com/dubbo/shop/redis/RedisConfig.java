package com.dubbo.shop.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class RedisConfig {

    @Value("${jedis.hosts}")
    String redis_hosts;

    @Value("${jedis.port}")
    Integer redis_port;

    @Bean
    public Jedis getRedisConn(){
        Jedis jedis = new Jedis(redis_hosts, redis_port);

        return jedis;

    }
}
