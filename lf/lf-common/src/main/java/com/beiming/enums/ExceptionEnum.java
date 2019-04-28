package com.beiming.enums;

import com.beiming.exception.ExceptionInterface;

public enum ExceptionEnum implements ExceptionInterface{
  UNKNOW_ERROR(-1,"未知错误"), 
  
  USER_HAS_EXIST(10001,"用户名已存在"),
  
  USER_NOT_EXIST(10001,"用户名未注册"),
  
  REGISTER_FAILE(10001,"注册失败"),
  
  PERMISSION_DENIED(22222,"权限不足，无法访问"),
  
  PERMISSION_DENIED1(22222,"用户名或密码错误"),

  SUCCESS(200,"SUCESS"), 
  ;
  private Integer code;

  private String message;

  private ExceptionEnum(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  @Override
  public Integer getCode() {
    return this.code;
  }

  @Override
  public String getMessage() {
    return this.message;
  }

  @Override
  public ExceptionInterface setMeaasge(String message) {
    this.message=message;
    return this;
  }


}
