package com.skp.redis;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisException;

import com.alibaba.fastjson.JSON;

/**
 * Redis Utility Operation
 * 
 * @author jin song
 *
 */
@Component(value = "RedisUtils")
public class RedisUtils implements InitializingBean {

    private static final Logger     logger                            = LoggerFactory.getLogger(RedisUtils.class);

    /**
     * 默认过期时间=30 天,单位秒
     */
    public static final int         DEFAULT_EXPIRE                    = 2592000;

    /**
     * 用户账号被禁用时间 =1天，单位秒
     */
    public static final int         MEMBERSHIP_FROZEN_EXPIRE          = 86400;

    /**
     * 用户登录失败检查时间 =1天，单位秒
     */
    public static final int         LOGIN_FAIL_FREQUENCY_CHECK_EXPIRE = 60;

    /**
     * 用户修改密码和重置密码检查时间 =1天，单位秒
     */
    public static final int         UPDATE_PW_FREQUENCY_CHECK_EXPIRE  = 60;

    /**
     * key的默认延期时间=365天，单位秒
     */
    public static final int         DEFAULT_EXPIRE_DELAY              = 31536000;

    /**
     * 永远不过期时间
     */
    public static final int         NEVER_EXPIRE                      = -1;

    /**
     * 全局静态 jedis
     */
    private static ShardedJedisPool jedisPool;

    /**
     * 注入用的pool
     */
    @Autowired(required = false)
    @Qualifier("shardedJedisPool")
    private ShardedJedisPool        shardedJedisPool;

    /**
     * 设置key-value对，值类型为String
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    public static void set(String key, String value, int expireTime) {
        ShardedJedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
            if (expireTime != NEVER_EXPIRE) {
                jedis.expire(key, expireTime);
            }
        } catch (JedisException je) {
            String errMsg = "Redis set操作异常, key:%s, value:%s";
            errMsg = String.format(errMsg, key, value);
            logger.error(errMsg, je);
        } finally {
            if (jedis != null)
                jedis.close();
        }

    }

    /**
     * 获取对应的key值，值类型为String
     * @param key
     * @return
     */
    public static String get(String key) {
        ShardedJedis jedis = jedisPool.getResource();
        try {
            return jedis.get(key);
        } catch (JedisException je) {
            String errMsg = "Redis get操作异常, key:%s";
            errMsg = String.format(errMsg, key);
            logger.error(errMsg, je);
            return null;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**
     * 移除对应的key值
     * @param key
     * @return
     */
    public static boolean remove(String key) {
        ShardedJedis jedis = jedisPool.getResource();
        try {
            jedis.del(key);
            return true;
        } catch (JedisException je) {
            String errMsg = "Redis del操作异常, key:%s";
            errMsg = String.format(errMsg, key);
            logger.error(errMsg, je);
            return false;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**
     * 判断key是否存在
     * 
     * @param key
     * @return
     */
    public static boolean exists(String key) {
        ShardedJedis jedis = jedisPool.getResource();
        try {
            return jedis.exists(key);
        } catch (JedisException je) {
            String errMsg = "Redis exists操作异常, key:%s";
            errMsg = String.format(errMsg, key);
            logger.error(errMsg, je);
            return false;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**
     * 设置key-value对，值类型为Object
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    public static boolean setT(String key, Object object) {
        ShardedJedis jedis = jedisPool.getResource();
        boolean result = false;
        try {
            String objectJson = JSON.toJSONString(object);
            jedis.set(key, objectJson);
            result = true;
        } catch (Exception je) {
            String errMsg = "Redis setT操作异常, key:%s";
            errMsg = String.format(errMsg, key);
            logger.error(errMsg, je);
        } finally {
            if (jedis != null)
                jedis.close();
        }
        return result;
    }

    /**
     * setT: <br/>
     * 设置key-value对，值类型为Object,带过期时间<br/>
     * @author liujie
     * @param key
     * @param object
     * @param expireTime
     * @return
     */
    public static boolean setT(String key, Object object, Integer expireTime) {
        ShardedJedis jedis = jedisPool.getResource();
        boolean result = false;
        try {
            String objectJson = JSON.toJSONString(object);
            jedis.set(key, objectJson);
            if (expireTime != NEVER_EXPIRE) {
                jedis.expire(key, expireTime);
            }
            result = true;
        } catch (Exception je) {
            String errMsg = "Redis setT操作异常, key:%s";
            errMsg = String.format(errMsg, key);
            logger.error(errMsg, je);
        } finally {
            if (jedis != null)
                jedis.close();
        }
        return result;
    }

    /**
     * 设置key-value对，值类型为Object
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    public static <T> T getT(String key, Class<T> clazz) {
        ShardedJedis jedis = jedisPool.getResource();
        T result = null;
        try {
            String objectJson = jedis.get(key);
            result = JSON.parseObject(objectJson, clazz);
        } catch (Exception je) {
            String errMsg = "Redis getT操作异常, key:%s";
            errMsg = String.format(errMsg, key);
            logger.error(errMsg, je);
        } finally {
            if (jedis != null)
                jedis.close();
        }
        return result;
    }

    /**
     * 设置key-field-value到hash表中，值类型为String
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    public static void hset(String key, String field, String value, int expireTime) {
        ShardedJedis jedis = jedisPool.getResource();
        try {
            jedis.hset(key, field, value);
            if (expireTime != NEVER_EXPIRE) {
                jedis.expire(key, expireTime);
            }
        } catch (JedisException je) {
            String errMsg = "Redis hset操作异常, key:%s, field:%s, value:%s";
            errMsg = String.format(errMsg, key, field, value);
            logger.error(errMsg, je);
        } finally {
            if (jedis != null)
                jedis.close();
        }

    }

    /**
     * hsetAll
     * 
     * @description 为多次设置redis中值的场景提供,防止频繁建立关闭连接
     */
    public static void hsetAll(String key, Map<String, String> fieldMap, int expireTime) {
        ShardedJedis jedis = jedisPool.getResource();

        try {
            jedis.hmset(key, fieldMap);
            if (expireTime != NEVER_EXPIRE) {
                jedis.expire(key, expireTime);
            }
        } catch (JedisException je) {
            String errMsg = "Redis hmset操作异常, key:%s:%s";
            errMsg = String.format(errMsg, key, JSON.toJSON(fieldMap));
            logger.error(errMsg, je);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**
     * 从hash表中获取对应的key-field值，值类型为String
     * @param key
     * @return
     */
    public static String hget(String key, String field) {
        ShardedJedis jedis = jedisPool.getResource();
        try {
            return jedis.hget(key, field);
        } catch (JedisException je) {
            String errMsg = "Redis hget操作异常, key:%s, field:%s";
            errMsg = String.format(errMsg, key, field);
            logger.error(errMsg, je);
            return null;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**
     * 从redis的hashtable中获取所有数据
     * 
     * @param key redis的key
     * @return
     */
    public static Map<String, String> hgetAll(String key) {
        ShardedJedis jedis = jedisPool.getResource();
        try {
            return jedis.hgetAll(key);
        } catch (JedisException je) {
            String errMsg = "Redis hgetAll操作异常, key:%s";
            errMsg = String.format(errMsg, key);
            logger.error(errMsg, je);
            return null;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**
     * 从hash表中移除对应的key-field
     * @param key
     * @return
     */
    public static boolean hdel(String key, String... field) {
        ShardedJedis jedis = jedisPool.getResource();
        try {
            jedis.hdel(key, field);
            return true;
        } catch (JedisException je) {
            String errMsg = "Redis hdel操作异常, key:%s";
            if (null != field) {
                int fieldSize = field.length;
                for (int i = 0; i < fieldSize; i++) {
                    errMsg += ", field:%s";
                }
            }
            errMsg = String.format(errMsg, key, field);
            logger.error(errMsg, je);
            return false;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**
     * 判断key是否存在
     * 
     * @param key
     * @return
     */
    public static boolean hexists(String key, String field) {
        ShardedJedis jedis = jedisPool.getResource();
        try {
            return jedis.hexists(key, field);
        } catch (JedisException je) {
            String errMsg = "Redis hexists操作异常, key:%s, field:%s";
            errMsg = String.format(errMsg, key, field);
            logger.error(errMsg, je);
            return false;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**
     * 设置过期时间
     * 
     * @param key
     * @return
     */
    public static boolean expire(String key, int seconds) {
        ShardedJedis jedis = jedisPool.getResource();
        try {
            jedis.expire(key, seconds);
            return true;
        } catch (JedisException je) {
            String errMsg = "Redis expire操作异常, key:%s, seconds:%s";
            errMsg = String.format(errMsg, key, seconds);
            logger.error(errMsg, je);
            return false;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**
     * 设置key-member 到set中
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    public static void sadd(String key, String member) {
        ShardedJedis jedis = jedisPool.getResource();
        try {
            jedis.sadd(key, member);
        } catch (JedisException je) {
            String errMsg = "Redis sadd操作异常, key:%s, member:%s";
            errMsg = String.format(errMsg, key, member);
            logger.error(errMsg, je);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**
     * key为自增值得情况
     * @param key
     * @param expireTime
     * @return
     *
     */
    public static int keyIncr(String key, int expireTime) {
        ShardedJedis jedis = jedisPool.getResource();
        try {
            Long value = null;
            if (!jedis.exists(key)) {
                value = jedis.incr(key);
                if (expireTime != NEVER_EXPIRE) {
                    jedis.expire(key, expireTime);
                }
            } else {
                value = jedis.incr(key);
            }
            return null == value ? 0 : value.intValue();
        } catch (JedisException je) {
            String errMsg = "Redis sadd操作异常, key:%s";
            errMsg = String.format(errMsg, key);
            logger.error(errMsg, je);
            return 0;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**
     * 查询member是否在key标记的set中
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    public static boolean sismember(String key, String member) {
        ShardedJedis jedis = jedisPool.getResource();
        try {
            return jedis.sismember(key, member);
        } catch (JedisException je) {
            String errMsg = "Redis sismember操作异常, key:%s, member:%s";
            errMsg = String.format(errMsg, key, member);
            logger.error(errMsg, je);
            return false;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**
     * 从key标记的set中随机返回一个member
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    public static String srandmember(String key) {
        ShardedJedis jedis = jedisPool.getResource();
        try {
            return jedis.srandmember(key);
        } catch (JedisException je) {
            String errMsg = "Redis srandmember操作异常, key:%s";
            errMsg = String.format(errMsg, key);
            logger.error(errMsg, je);
            return null;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**
     * 从set中删除一个member
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    public static void srem(String key, String member) {
        ShardedJedis jedis = jedisPool.getResource();
        try {
            jedis.srem(key, member);
        } catch (JedisException je) {
            String errMsg = "Redis srem操作异常, key:%s, member:%s";
            errMsg = String.format(errMsg, key, member);
            logger.error(errMsg, je);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**
     * 持久化
     * @param key
     * @return
     */
    public static Long persist(String key) {
        ShardedJedis jedis = jedisPool.getResource();
        try {
            return jedis.persist(key);
        } catch (JedisException je) {
            String errMsg = "Redis persist操作异常, key:%s";
            errMsg = String.format(errMsg, key);
            logger.error(errMsg, je);
            return 0L;
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**
     * getJedisPoolStatus: <br/>
     * 获取当前连接池状态<br/>
     * @author liujie
     * @return
     */
    public static Map<String, Integer> getJedisPoolStatus() {
        Map<String, Integer> statusMap = new HashMap<String, Integer>();
        statusMap.put("numActive", jedisPool.getNumActive());
        statusMap.put("numIdle", jedisPool.getNumIdle());
        statusMap.put("numWaiters", jedisPool.getNumWaiters());
        return statusMap;
    }

    /**
     * zremrangeByScore: <br/>
     * 删除zset类型score在指定范围内的元素<br/>
     * @param key
     * @param min
     * @param max
     */
    public static void zremrangeByScore(String key, Integer min, Integer max) {
        ShardedJedis jedis = jedisPool.getResource();
        try {
            jedis.zremrangeByScore(key, min, max);
        } catch (JedisException je) {
            String errMsg = "Redis zremrangeByScore操作异常, key:%s";
            errMsg = String.format(errMsg, key);
            logger.error(errMsg, je);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**
     * zadd: <br/>
     * zset类型添加操作 <br/>
     * @param key
     * @param score
     * @param member
     * @param expireTime
     */
    public static void zadd(String key, Integer score, String member, int expireTime) {
        ShardedJedis jedis = jedisPool.getResource();
        try {
            jedis.zadd(key, score, member);
            if (expireTime != NEVER_EXPIRE) {
                jedis.expire(key, expireTime);
            }
        } catch (JedisException je) {
            String errMsg = "Redis zadd操作异常, key:%s";
            errMsg = String.format(errMsg, key);
            logger.error(errMsg, je);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    /**
     * zcount: <br/>
     * zset类型范围数量 <br/>
     * @param key
     * @param min
     * @param max
     * @return
     */
    public static Long zcount(String key, Integer min, Integer max) {
        ShardedJedis jedis = jedisPool.getResource();
        Long count = 0L;
        try {
            count = jedis.zcount(key, min, max);
        } catch (JedisException je) {
            String errMsg = "Redis zcount操作异常, key:%s";
            errMsg = String.format(errMsg, key);
            logger.error(errMsg, je);
        } finally {
            if (jedis != null)
                jedis.close();
        }

        return count;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool = shardedJedisPool;
    }
}
