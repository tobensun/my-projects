package ltd.pdx.config.redis.support;

import ltd.pdx.config.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * @author buyanping
 */
@Component
public class RedisServiceSupport implements RedisService {
    @Autowired
    private JedisCluster jedisCluster;

    public static final Random RANDOM = new Random();

    @Override
    public String set(final String key, final String value) {
        return jedisCluster.set(key, value);
    }

    @Override
    public String get(final String key) {
        return jedisCluster.get(key);
    }

    @Override
    public Boolean exists(final String key) {
        return jedisCluster.exists(key);
    }

    @Override
    public Long expire(final String key, final int seconds) {
        return jedisCluster.expire(key, seconds);
    }

    @Override
    public Long ttl(final String key) {
        return jedisCluster.ttl(key);
    }

    @Override
    public Long incr(final String key) {
        return jedisCluster.incr(key);
    }

    @Override
    public void sadd(final String key, final String... values) {
        jedisCluster.sadd(key, values);
    }

    @Override
    public Set<String> smembers(final String key) {
        return jedisCluster.smembers(key);
    }

    @Override
    public Long scard(final String key) {
        return jedisCluster.scard(key);
    }

    @Override
    public List<String> srandmembers(final String key, final int count) {
        return jedisCluster.srandmember(key, count);
    }

    @Override
    public Long srem(final String key, final String... value) {
        return jedisCluster.srem(key, value);
    }

    @Override
    public Long hset(final String key, final String field, final String value) {
        return jedisCluster.hset(key, field, value);
    }

    @Override
    public String hget(final String key, final String field) {
        return jedisCluster.hget(key, field);
    }

    @Override
    public Long hdel(final String key, final String... field) {
        return jedisCluster.hdel(key, field);
    }

    /**
     * 获取key对应的value值,并更新
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public String getSet(String key, String value) {
        return jedisCluster.getSet(key, value);
    }

    /**
     * @Description: 获取锁 @Title: lock @param timeout 获取锁超时时间 @param expire
     * 锁的超时时间(秒)，过期删除 @return String @throws
     */
    public String lock(String key, long timeout, int expire) {
        String retIdentifier = null;
        try {
            String lockKey = "lock:" + key;
            // 随机生成一个value
            String identifier = UUID.randomUUID().toString();
            // 获取锁的超时时间，超过这个时间则放弃获取锁
            long end = System.currentTimeMillis() + timeout;
            while (System.currentTimeMillis() < end) {
                long i = jedisCluster.setnx(lockKey, identifier);
                if (i == 1) {
                    this.jedisCluster.expire(lockKey, expire);
                    retIdentifier = identifier;
                    return retIdentifier;
                } else {
                    System.out.println("获取锁失败");
                }
                // 短暂休眠，避免出现活锁
                Thread.sleep(3, RANDOM.nextInt(500));
            }
        } catch (Exception e) {
            throw new RuntimeException("Locking error", e);
        }
        return retIdentifier;
    }

    /**
     * @Description: 释放锁 @Title: unlock @param identifier 锁标识 void
     * 无论是否加锁成功，都需要调用unlock，应以lock();try{doSomething();}finally{unlock();}的方式调用 @throws
     */
    public void unlock(String key, String identifier) {
        try {
            String lockKey = "lock:" + key;
            // 通过前面返回的value值判断是不是该锁，若是该锁，则删除，释放锁
            if (identifier.equals(jedisCluster.get(lockKey))) {
                this.jedisCluster.del(lockKey);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unlock error", e);
        }
    }
}
