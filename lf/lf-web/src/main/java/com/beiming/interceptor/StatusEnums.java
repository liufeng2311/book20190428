package com.beiming.interceptor;

/**
 * Created by zli on 2018/9/3.
 *
 * 表里status值
 */
public enum StatusEnums {
  NORMAL("0"),
  DELETE("1");

  private String value;

  StatusEnums(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
