package com.beiming.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.beiming.dto.request.InsertUserRequestDTO;
import com.beiming.entity.User;
import com.beiming.mapper.UserMapper;
import com.beiming.service.UserService;
import tk.mybatis.mapper.entity.Example;
@Service
@CacheConfig(cacheNames = "redis") //使用缓存注解时，必须规定缓存的名字，用户分空间存储，通过@CacheConfig统一配置
public class UserSrviceImpl implements UserService{

  private static final Logger Logger = LoggerFactory.getLogger(UserSrviceImpl.class);
  @Autowired
  private UserMapper userMapper;
  
  @Autowired
  RedisTemplate<String, Object> redisTemple;

  @Override
  //@Cacheable(key = "123")
  public List<User> getUserList() {
    Logger.info("==========查询数据库=========");
    List<User> userList = userMapper.selectAll();
    User user2 = userMapper.selectByPrimaryKey(1);
    //user2.setVersion(user2.getVersion());
    //userMapper.getUser1(user2.getId().toString(), "xml#{}");
    //userMapper.getUser2(user2.getId().toString(), "xml${}");
    //userMapper.updateByPrimaryKey(user2);
    user2.setUsername("admin1");
    user2.setPassword("admin1");
    Example example = new Example(User.class);
    example.createCriteria().andEqualTo("username", "admin");
    userMapper.updateByExample(user2, example);
    //userMapper.updateByExample(user2, example)
    //userMapper.getUser2("1","12");
    return userList;
  }

  @Override
  @CachePut(key = "123") //RedisConnectionFactory  //RedisConnection
  public void insertUser(InsertUserRequestDTO insertUser) {
    User user = new User();
    BeanUtils.copyProperties(insertUser, user);
    userMapper.insert(user);
  }

  @Override
  @CacheEvict(key = "123")
  public void deleteUser(String userId) {
    userMapper.deleteByPrimaryKey(userId);    
  }

  @Override
  public List<User> getUserList1() {
    redisTemple.opsForValue().set("redisTemple", "redisTemple123213"); //
    redisTemple.expire("redisTemple", 60, TimeUnit.MINUTES);
    redisTemple.opsForHash().put("hash", "qiaotiantian", "xiaokeai");
    redisTemple.expire("hash", 600, TimeUnit.MINUTES);
    List<User> userList = userMapper.selectAll();
    String jsonString = JSON.toJSONString(userList);
    redisTemple.opsForValue().set("redisTemple123", jsonString);
    User user = userMapper.selectByPrimaryKey("1");
    redisTemple.opsForValue().set("user", user); //序列化是字符串时,直接存储对象会报错
    System.out.println(redisTemple.opsForValue().get("redisTemple"));
    System.out.println(redisTemple.opsForHash().values("hash"));
    System.out.println(redisTemple.opsForHash().entries("hash"));
    System.out.println(redisTemple.opsForValue().get("redisTemple123"));
    System.out.println(redisTemple.opsForValue().get("user"));
    return userList;
  }

}
