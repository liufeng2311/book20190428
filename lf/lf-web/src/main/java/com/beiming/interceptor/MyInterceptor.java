package com.beiming.interceptor;

public class MyInterceptor implements Interceptor{

  @Override
  public boolean before() {
    System.out.println("before......");
    return false;
  }

  @Override
  public void after() {
    System.out.println("after......");
    
  }

  @Override
  public Object around(Invocation invocation) throws Exception {
    System.out.println("around before......");
    Object object = invocation.proceed();
    System.out.println("around after......");
    return object;
  }

  @Override
  public void afterReturning() {
    System.out.println("afterReturning......");
    
  }

  @Override
  public void afterThrowing() {
    System.out.println("afterThrowing......");
    
  }

  @Override
  public boolean useAround() {
    // TODO Auto-generated method stub
    return true;
  }

}
