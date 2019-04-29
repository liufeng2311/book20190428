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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@Configuration
@EnableCaching//开始缓存注解的功能
public class RedisConfig {

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
      RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
      redisTemplate.setConnectionFactory(redisConnectionFactory);
      FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<Object>(Object.class);
      ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
      redisTemplate.setValueSerializer(fastJsonRedisSerializer);
      redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
      redisTemplate.setKeySerializer(new StringRedisSerializer());
      redisTemplate.setHashKeySerializer(new StringRedisSerializer());
      redisTemplate.afterPropertiesSet();
      return redisTemplate;
  }
   
  //自定义配置缓存管理器，配置文件中的配置失效
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
}
