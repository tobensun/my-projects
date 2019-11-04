package ltd.pdx.config.redis;

import java.util.List;
import java.util.Set;

/**
 * @author buyanping
 */
public interface RedisService {
    String set(String key, String value);

    String get(String key);

    Boolean exists(String key);

    Long expire(String key, int seconds);

    Long ttl(String key);

    Long incr(String key);

    Long srem(String key, String... value);

    Long hset(String key, String field, String value);

    String hget(String key, String field);

    Long hdel(String key, String... field);

    void sadd(String key, String... values);

    Set<String> smembers(String key);

    Long scard(String key);

    String getSet(String key, String value);

    List<String> srandmembers(String key, int count);

    /**
     * @param @param  key
     * @param @param  timeout 获取锁的时间 毫秒，一般为300-500ms
     * @param @param  expire 锁超时时间 s,一般为1s
     * @param @return
     * @return String
     * @Description: 获取锁
     * @author eagle
     * @date 2019年9月2日
     * @version V1.0
     */
    String lock(String key, long timeout, int expire);

    /**
     * @param @param key 锁的key
     * @param @param identifier 随机数
     * @return void
     * @Description: 释放锁
     * @author eagle
     * @date 2019年9月2日
     * @version V1.0
     */
    void unlock(String key, String identifier);
}
