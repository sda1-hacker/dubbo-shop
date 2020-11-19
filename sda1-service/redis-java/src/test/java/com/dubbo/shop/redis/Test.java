package com.dubbo.shop.redis;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    // 事务：multi - exec       multi 之后 的命令表示在一个事务里面， 通过exec提交事务
    @org.junit.Test
    public void spring_data_redis_transaction_Test(){

        redisTemplate.setValueSerializer(new StringRedisSerializer());

        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                // 开启事务
                redisOperations.multi();

                // 执行操作
//                redisOperations.opsForList().leftPush("student:1:fans", "3");
//                redisOperations.opsForList().leftPush("student:3:follows", "1");

                System.out.println("提交事务之前" + redisTemplate.opsForList().range("student:1:fans", 0, -1));

                // 提交事务
                redisOperations.exec();

                System.out.println("提交事务之后" + redisTemplate.opsForList().range("student:1:fans", 0, -1));

                return null;
            }
        });
    }

    // 一般模式批量插入数据
    @org.junit.Test
    public void noPipeLineTest(){
        long start = System.currentTimeMillis();

        for(int i = 0; i < 10000; i++){
            // redisTemplate.opsForValue().set("q" + i, "p" + i);
            redisTemplate.delete("q" + i);
        }

        long end = System.currentTimeMillis();
        System.out.println(end - start); // 8160
    }

    // 流水线模式， 适用于批量插入数据，减少与redis交互时间，从而提高效率
    @org.junit.Test
    public void pipeLineTest(){
        long start = System.currentTimeMillis();
        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                for (int i = 0; i < 10000; i++){
                    // redisOperations.opsForValue().set("k" + i, "v" + i);
                    redisOperations.delete("k" + i);
                }
                return null;
            }
        });
        long end = System.currentTimeMillis();

        System.out.println(end - start); // 143
    }

    // 设置超时时间
    @org.junit.Test
    public void redisTtlTest(){
        redisTemplate.opsForValue().set("kk", "99999");
        System.out.println(redisTemplate.getExpire("kk"));
        redisTemplate.expire("kk", 5, TimeUnit.MINUTES); //设置过期时间  ：  key, 时间大小, 时间单位
        System.out.println(redisTemplate.getExpire("kk")); // 获取过期时间，-1 表示没有过i时间，-2表示已过期，
    }
}
