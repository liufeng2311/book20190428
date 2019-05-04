package com.beiming.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.beiming.entity.User;
import com.beiming.service.AopService;

@Service
public class AopServiceImpl implements AopService{

  private final static Logger logger = LoggerFactory.getLogger(AopServiceImpl.class);
  @Override
  public void HelloAop(String name) {
    logger.info("name= {}",name);
    
  }
  @Override
  public User PrintUser(User user) {
    if(user == null) {
      throw new RuntimeException("用户为空");
    }
    logger.info("id= {}",user.getId());
    logger.info("username= {}",user.getUsername());
    logger.info("password= {}",user.getPassword());
    return user;
  }

}
