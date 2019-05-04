package com.beiming.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.beiming.entity.User;
import com.beiming.service.AopService;
import com.beiming.service.UserValidatorService;

@RestController
@Controller
@RequestMapping("/aop")
public class AopController {

  @Autowired
  AopService aopService;
  
  @PostMapping("/printUser")
  public User printUser(@RequestBody User user) {
    UserValidatorService userValidatorService =(UserValidatorService) aopService;
    if(userValidatorService.validator(user)) {
      user.setId(123);
      user.setUsername("123");
      user.setPassword("123");
      aopService.PrintUser(user);
    }
    return user;
    
  }

}
