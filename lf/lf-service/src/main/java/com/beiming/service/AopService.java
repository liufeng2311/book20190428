package com.beiming.service;

import com.beiming.entity.User;

public interface AopService {

  void HelloAop(String name);
  
  User PrintUser(User user);
}
