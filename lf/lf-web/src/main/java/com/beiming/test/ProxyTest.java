package com.beiming.test;

import com.beiming.interceptor.MyInterceptor;
import com.beiming.interceptor.ProxyBean;
import com.beiming.service.AopService;
import com.beiming.service.impl.AopServiceImpl;

public class ProxyTest {

  public static void main(String[] args) {
    AopService aop = new AopServiceImpl();
    aop.HelloAop("aop");
    System.out.println("=================================");
    MyInterceptor interceptor = new MyInterceptor();
    AopService proxyBean = (AopService) ProxyBean.getProxyBean(aop, interceptor);
    proxyBean.HelloAop("aop");
  }
  
}
