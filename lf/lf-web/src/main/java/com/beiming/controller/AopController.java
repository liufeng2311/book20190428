package com.beiming.controller;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
  
  @PostMapping(value = "/printUser" ,consumes = MediaType.ALL_VALUE ,produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(code = HttpStatus.CREATED) //修改返回code
  public User printUser(@RequestBody User user,@NumberFormat(pattern = "#,###,###") Double number) {
    UserValidatorService userValidatorService =(UserValidatorService) aopService;
    HttpHeaders headers = new HttpHeaders();
    headers.add("as", "as");
    if(userValidatorService.validator(user)) {
      user.setId(123);
      user.setUsername("123");
      user.setPassword("123");
      aopService.PrintUser(user);
    }
    return user;
    
  }

}
