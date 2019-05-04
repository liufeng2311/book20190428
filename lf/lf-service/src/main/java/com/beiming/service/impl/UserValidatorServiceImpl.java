package com.beiming.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.beiming.entity.User;
import com.beiming.service.UserValidatorService;

@Service
public class UserValidatorServiceImpl implements UserValidatorService{

  private final static Logger logger = LoggerFactory.getLogger(UserValidatorServiceImpl.class);
  @Override
  public boolean validator(User user) {
    logger.info("aop引入接口功能.....");
    return user != null;
  }

}
