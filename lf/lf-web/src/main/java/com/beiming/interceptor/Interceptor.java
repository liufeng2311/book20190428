package com.beiming.interceptor;


public interface Interceptor {

  /**
   * 事前方法
   * @return
   */
  boolean before();
  
  /**
   * 事后方法
   */
  void after();
  
  /**
   * 取代原有方法
   * @param invocation
   * @return
   * @throws Exception
   */
  Object around(Invocation invocation) throws Exception; //Invocation源码解读
  
  /**
   * 事后返回方法，没有异常执行
   */
  void afterReturning();
  
  /**
   * 事后返回方法，有异常执行
   */
  void afterThrowing();
  
  /**
   * 是否取代原有方法
   * @return
   */
  boolean useAround();
}
