package com.beiming.interceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.Data;

@Data
public class Invocation {

  private Object[] params;
  
  private Method method;
  
  private Object target;
  
  public Object proceed() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    return method.invoke(target, params);
  }

  public Invocation(Object[] params, Method method, Object target) {
    super();
    this.params = params;
    this.method = method;
    this.target = target;
  }
  
}
