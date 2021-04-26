package cn.pioneeruniverse.common.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

@Component
public final class RedisUtils {

    private static final Logger log = LoggerFactory.getLogger(RedisUtils.class);

    @Autowired
    private RedisTemplate<Serializable, Object> redisTemplate;

    @Autowired
    private RedisTemplate<Serializable, Serializable> serializeRedisTemplate;


    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    public Set getKeys(String keyPrefix) {
        return redisTemplate.keys(keyPrefix + "*");
    }

    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }


    public void remove(final String key) {
        if (StringUtils.isNotEmpty(key) && exists(key)) {
            redisTemplate.delete(key);
        }
    }
    
    public void removeList(final String key) {
    	Set<Serializable> keys = redisTemplate.keys(key);
        if (CollectionUtil.isNotEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }

    public void removeForSerializable(final String key){
        if (StringUtils.isNotEmpty(key) && exists(key)) {
            serializeRedisTemplate.delete(key);
        }
    }


    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    public boolean existsForSerializable(final String key) {
        return serializeRedisTemplate.hasKey(key);
    }


    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate
                .opsForValue();
        result = operations.get(key);
        return result;
    }

    public <T> T getForSerializable(final String key, Class<T> requiredType) {
        ValueOperations<Serializable, Serializable> operations = serializeRedisTemplate.opsForValue();
        return (T) operations.get(key);
    }


    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate
                    .opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            log.error("redisUtils set error,cause:" + e.getMessage(), e);
        }
        return result;
    }

    /**
     * @param key
     * @param value
     * @return boolean
     * @Description redis存储实现序列的对象
     * @MethodName set
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2019/4/16 13:48
     */
    public boolean setForSerializable(final String key, Serializable value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Serializable> operations = serializeRedisTemplate
                    .opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            log.error("redisUtils set error,cause:" + e.getMessage(), e);
        }
        return result;
    }

    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            log.error("redisUtils set error,cause:" + e.getMessage(), e);
        }
        return result;
    }

    public boolean setForSerializable(final String key, Serializable value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Serializable> operations = serializeRedisTemplate.opsForValue();
            operations.set(key, value);
            serializeRedisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            log.error("redisUtils set error,cause:" + e.getMessage(), e);
        }
        return result;
    }

    public void setForHash(final String key, final String hashKey, Object value) {
        HashOperations<Serializable, String, Object> opsForHash = redisTemplate.opsForHash();
        opsForHash.put(key, hashKey, value);
    }

    public Object getForHash(final String key, final String hashKey) {
        HashOperations<Serializable, String, Object> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hashKey);
    }

    public void delForHash(final String key, final String hashKey) {
        HashOperations<Serializable, String, Object> opsForHash = redisTemplate.opsForHash();
        if (opsForHash.hasKey(key, hashKey)) {
            opsForHash.delete(key, hashKey);
        }
    }

    public String getType(final String key) {
        DataType dataType = redisTemplate.type(key);
        return dataType.code();
    }

    public void expire(final String key, Long expireTime) {
        try {
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("redisUtils set error,cause:" + e.getMessage(), e);
        }
    }
}