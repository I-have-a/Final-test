package com.niuma.questionnaire.utils;

import com.alibaba.fastjson2.JSON;
import com.niuma.questionnaire.common.RedisData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.niuma.questionnaire.common.RedisConstant.CACHE_NULL_TIME;


/**
 * 缓存工具类
 * 通过部分手法减小缓存击穿和缓存穿透问题，简化代码书写
 */
@Slf4j
@Component
public class CacheClient {

    public static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);
    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
     * 存入redis，免转JSON
     *
     * @param key   键
     * @param value 值
     * @param time  过期时间
     * @param unit  时间单位
     */
    public void set(String key, Object value, Long time, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(value), time, unit);
    }

    /**
     * 设置键永不过期
     *
     * @param key   键
     * @param value 值
     */
    public void setKeyForever(String key, Object value) {
        stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(value));
    }

    /**
     * 为热点数据设置逻辑过期
     *
     * @param key   键
     * @param value 值
     * @param time  时间
     * @param unit  时间单位
     */
    public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit unit) {
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));
        stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(redisData));
    }

    /**
     * @param keyPrefix  键前缀
     * @param id         对象ID
     * @param type       实体类型
     * @param dbFallback 数据库查询方法
     * @return 如果redis中存在就则返回查询数据
     * 若redis中不存在便去数据库中查询
     * 数据库中没有就在redis中存空值
     * 往后再查便不需要重复往数据库中查询
     * 防止缓存穿透
     */
    public <T, ID, LT> T queryWithPassThrough(
            String keyPrefix, ID id, Class<T> type, Function<ID, T> dbFallback, Long time, TimeUnit unit, Class<LT> listItem) {
        String key = keyPrefix + id;
        String json = stringRedisTemplate.opsForValue().get(key);
        if (json != null) {
            if (!"".equals(json)) {
                if (type == List.class) return (T) JSON.parseArray(json, listItem);
                return JSON.parseObject(json, type);
            } else return null;
        }
        T t = dbFallback.apply(id);
        if (t == null) {
            stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TIME, TimeUnit.MINUTES);
            return null;
        }
        if (time == null && unit == null) this.setKeyForever(key, t);
        else this.set(key, t, time, unit);
        return t;
    }

    /**
     * @param keyPrefix  键前缀
     * @param id         对象ID
     * @param type       实体类型
     * @param dbFallback 数据库查询方法
     * @param time       有效时间
     * @param unit       时间单位
     * @param lockPrefix 锁前缀
     * @return 热点内容使用逻辑更新方式存入缓存
     * 避免热点内容TTL到期失效 产生大量请求打到数据库
     */
    public <T, ID> T queryWithLogicalExpire(
            String keyPrefix, ID id, Class<T> type, Function<ID, T> dbFallback, Long time, TimeUnit unit, String lockPrefix) {
        String key = keyPrefix + id;
        String json = stringRedisTemplate.opsForValue().get(key);
        if ("".equals(json)) return null;
        RedisData redisData = JSON.parseObject(json, RedisData.class);
        T t = JSON.parseObject(String.valueOf(redisData.getData()), type);
        LocalDateTime expireTime = redisData.getExpireTime();
        if (expireTime.isAfter(LocalDateTime.now())) return t;
        String localKey = lockPrefix + id;
        boolean lock = tryLock(localKey);
        if (lock) {
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try {
                    T t1 = dbFallback.apply(id);
                    this.setWithLogicalExpire(key, t1, time, unit);
                } catch (Exception ex) {
                    throw new RuntimeException();
                } finally {
                    unlock(localKey);
                }
            });
        }
        return t;
    }

    /**
     * 放锁
     *
     * @param key 锁名
     * @return 获取结果
     */
    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtils.isTrue(flag);
    }

    /**
     * @param key 删除锁
     */
    private void unlock(String key) {
        stringRedisTemplate.delete(key);
    }
}
