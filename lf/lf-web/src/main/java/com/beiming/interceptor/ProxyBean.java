package com.beiming.interceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyBean implements InvocationHandler{

  private Object target = null ;
  
  private Interceptor interceptor = null;
  
  public static Object getProxyBean(Object target, Interceptor interceptor) {
    ProxyBean proxyBean = new ProxyBean();
    proxyBean.target = target;
    proxyBean.interceptor = interceptor;
    Object proxy = Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), proxyBean);
    return proxy;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    boolean exceptionFlag = false;
    Invocation invocation = new Invocation(args, method,target);
    Object retObj = null;
    if(interceptor.useAround()) {
      retObj = interceptor.around(invocation);
    }else {
      retObj = method.invoke(target, args);
    }
    interceptor.after();
    if(exceptionFlag) {
      interceptor.afterThrowing();
    }else {
      interceptor.afterReturning();
      return retObj;
    }
    return null;
  }
}
