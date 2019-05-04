package com.beiming.config;

import java.time.Duration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableCaching//开始缓存注解的功能
public class RedisConfig {

  RedisConnectionFactory redisConnectionFactory = null ; //创建工厂时不可以直接作为参数传入方法，那样spring会为我们创建一个工厂类，造成循环
  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
      RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
      redisTemplate.setKeySerializer(redisTemplate.getStringSerializer()); //获取redisTemplate自己的字符串序列化并赋值到key和value
      //redisTemplate.setValueSerializer(redisTemplate.getStringSerializer());
      //redisTemplate.setValueSerializer(new FastJsonRedisSerializer<Object>(Object.class));
      redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
      redisTemplate.setHashKeySerializer(redisTemplate.getStringSerializer()); 
      redisTemplate.setHashValueSerializer(redisTemplate.getStringSerializer());
      redisTemplate.setConnectionFactory(redisConnectionFactory);
      /*FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<Object>(Object.class);
      ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
      redisTemplate.setValueSerializer(fastJsonRedisSerializer);
      redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
      redisTemplate.setKeySerializer(new StringRedisSerializer());
      redisTemplate.setHashKeySerializer(new StringRedisSerializer());
      redisTemplate.afterPropertiesSet();*/
      return redisTemplate;
  }
   
  //自定义配置缓存管理器,配置文件中的配置失效
    @Bean(value="ll")
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)).disableKeyPrefix(); // 设置缓存有效期一小时
        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration).build();
    }
    
    //自定义缓存管理器
    @Bean(value = "lf")
    @Primary
    public CacheManager initRedisCacheManager(RedisConnectionFactory redisConnectionFactory) {
      RedisCacheWriter cacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory); //创建加锁的写入器
      RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig(); //获取缓存默认的配置,并添加如下配置，类似配置文件
      //config.serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer())); //规定序列化
      //以下赋值不是set方法，每次都是新建，类似于build
      config = config.disableKeyPrefix(); //禁用前缀
      config = config.entryTtl(Duration.ofMinutes(3)); //设置缓存三分钟
      RedisCacheManager redisCacheManager = new  RedisCacheManager(cacheWriter, config);
      return redisCacheManager;
    }
    
    
    //创建redis连接工厂,每次需要从工厂获取连接，为了简化，spring提供了RedisTemplate，使用时，他每次都是从工厂里取一条连接
    @Bean(name = "RedisConnectionFactory")
    public RedisConnectionFactory initRedisConnectionFactory() {
      if(redisConnectionFactory != null) {
        return redisConnectionFactory;
      }
      JedisPoolConfig pool = new JedisPoolConfig();
      pool.setMaxIdle(30);  //最大空闲数
      pool.setMaxTotal(50); //最大连接数
      pool.setMaxWaitMillis(2000); //最大等待毫秒数
      JedisConnectionFactory jedis = new JedisConnectionFactory(pool);
      RedisStandaloneConfiguration configuration = jedis.getStandaloneConfiguration();
      configuration.setHostName("127.0.0.1");
      configuration.setPort(6379);
      return jedis;
      
    }
}
