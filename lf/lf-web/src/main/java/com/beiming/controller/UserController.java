package com.beiming.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.beiming.dto.request.InsertUserRequestDTO;
import com.beiming.service.UserService;
import com.beiming.util.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RestController
@Api(value = "UserController", tags = "用户接口")
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @ApiOperation(value = "获取用户列表", notes = "获取用户列表")
  @RequestMapping(value = "/getUser", method = RequestMethod.GET)
  public ResultModel getUserList(){
    return ResultModel.success(userService.getUserList());
  }
  
  @ApiOperation(value = "获取用户列表", notes = "获取用户列表")
  @RequestMapping(value = "/getUser1", method = RequestMethod.GET)
  public ResultModel getUserList1(){
    return ResultModel.success(userService.getUserList1());
  }
  
  @ApiOperation(value = "获取用户列表", notes = "获取用户列表")
  @RequestMapping(value = "/insertUser", method = RequestMethod.POST)
  public ResultModel insertUser(@RequestBody @Valid InsertUserRequestDTO user){
    userService.insertUser(user);
    return ResultModel.success(null);
  }
  
  @ApiOperation(value = "获取用户列表", notes = "获取用户列表")
  @RequestMapping(value = "/deleteUser/{id}", method = RequestMethod.GET)
  public ResultModel deleteUser(@ApiParam(value = "id", required =true) @PathVariable("id") String id){
    userService.deleteUser(id);
    return ResultModel.success(null);
  }
}
