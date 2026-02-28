package com.mipt.sem2.todolist.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

  private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

  @Around("execution(* com.mipt.sem2.service.*.*(..))")
  public Object logServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
    String methodName = joinPoint.getSignature().toShortString();
    Object[] args = joinPoint.getArgs();
    log.info("started: {}.() | Аргументы: {}", methodName, Arrays.toString(args));

    long start = System.currentTimeMillis();
    try {
      Object result = joinPoint.proceed();
      long duration = System.currentTimeMillis() - start;
      log.info("Exiting method: " + methodName + " with result: " + result + " (duration: " + duration + " ms)");
      return result;
    } catch (Throwable e) {
      log.info("Exception in method: " + methodName + " - " + e.getMessage());
      throw e;
    }
  }
}
