package com.beiming.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.beiming.dto.request.InsertUserRequestDTO;
import com.beiming.entity.User;
import com.beiming.mapper.UserMapper;
import com.beiming.service.UserService;
@Service
@CacheConfig(cacheNames = "redisName") //使用缓存注解时，必须规定缓存的名字，用户分空间存储，通过@CacheConfig统一配置
public class UserSrviceImpl implements UserService{

  private static final Logger Logger = LoggerFactory.getLogger(UserSrviceImpl.class);
  @Autowired
  private UserMapper userMapper;

  @Override
  @Cacheable(key = "123")
  public List<User> getUserList() {
    Logger.info("==========查询数据库=========");
    List<User> userList = userMapper.selectAll();
    return userList;
  }

  @Override
  @CachePut(key = "123")
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

}
