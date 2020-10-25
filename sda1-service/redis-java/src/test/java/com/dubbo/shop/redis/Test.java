package com.dubbo.shop.redis;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {

    @Autowired
    public Jedis jedis;

    @Autowired
    private RedisTemplate redisTemplate;

    @org.junit.Test
    public void redisTest(){
        jedis.set("wh", "hahahahaha");
    }

    @org.junit.Test
    public void spring_data_redis_Test(){
        // 使用spring data redis 的时候
        // 会对 key 和 value 都做序列化操作，  因此再redis-cli不能直接通过 key 去获取数据
        // 可以通过修改配置，来修改序列化方式。

        redisTemplate.opsForValue().set("wq", "12333");
        // redisTemplate.delete("ww");
        System.out.println(redisTemplate.opsForValue().get("ww"));
    }

    @org.junit.Test
    public void spring_data_redis_increment_Test(){

        // 执行increment 叠加操作，  value 的序列化必须要是String类型的
        redisTemplate.setValueSerializer(new StringRedisSerializer()); // 局部配置

        redisTemplate.opsForValue().set("all_money", "100");    // 这里也要是String
        System.out.println(redisTemplate.opsForValue().get("all_money"));
        redisTemplate.opsForValue().increment("all_money", 100);
        System.out.println(redisTemplate.opsForValue().get("all_money"));
    }

    @org.junit.Test
    public void spring_data_redis_hash_Test(){

        // hash 相当于 mapping
        // key ->  { hash_key -> { hash_value } }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "张三");
        map.put("age", "19");

        // 序列化hash的key为String  -- 这里只是做了局部的配置
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // 序列化hash的val为String  -- 这里只是做了局部的配置
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        // redisTemplate.opsForHash().putAll("person:1", map);
        System.out.println(redisTemplate.opsForHash().entries("person:1").get("name"));
    }
}
