package com.beiming.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.DeclareParents;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.beiming.entity.User;
import com.beiming.service.UserValidatorService;
import com.beiming.service.impl.UserValidatorServiceImpl;

@Aspect
@Component
public class MyAspect implements Ordered{ //Ordered接口用来确定多个切面的执行顺序，1>2>3，或者使用注解@Order(1)，多个切面是嵌套执行的，而不是顺序执行，即   before1   >   before2   > after2  >after1

  private final static Logger logger = LoggerFactory.getLogger(MyAspect.class);
  
  @DeclareParents(value = "com.beiming.service.impl.AopServiceImpl",  //value表示要增强的类
      defaultImpl = UserValidatorServiceImpl.class)                          //value类的增强类
  public UserValidatorService userValidator;
  
  @Pointcut("execution(* com.beiming.service.impl.AopServiceImpl.PrintUser(..))")      //定义切点
  public void pointCut() {
    
  }
  
  @Before("pointCut()&&args(user)")                                                                //前置通知(发生在方法前，发生异常，该通知结束，不影响其他通知)
  public void before(JoinPoint joinpoint, User user) {
    logger.info("before...");
  }
  
  @After("pointCut()")                                                                //后置通知前置通知(发生在方法后，发生异常，该通知结束，不影响其他通知)
  public void after() {
    logger.info("after...");
  }
  
  @AfterReturning(value = "pointCut()" ,returning = "val")                                                       //后置返回通知(发生在@After后，方法没有发生异常执行)
  public void afterReturning(Object val) {    //当有环绕通知时，取得是环绕通知方法的返回值
    logger.info("afterReturning...");
  }
  
  @AfterThrowing("pointCut()")                                                        //后置异常通知(发生在@After后，方法发生异常执行)
  public void afterThrowing() {
    logger.info("afterThrowing...");
  }

  
  @Around("pointCut()")                                                               //环绕通知(用于大规模修改原有方法时使用，可通过proceed方法回调原来的方法，环绕通知的方法优先于@Before和@Before)        
  public void around(ProceedingJoinPoint point) throws Throwable {
    logger.info("around before...");                                                  //反生在@Before方法之前
    point.proceed(); //调用原有方法
    logger.info("around after...");                                                   //反生在@After方法之前
  }
  
  @Override
  public int getOrder() {
    // TODO Auto-generated method stub
    return 1;
  }
}
