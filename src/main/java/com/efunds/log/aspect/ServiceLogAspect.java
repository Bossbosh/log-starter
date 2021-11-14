package com.efunds.log.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class ServiceLogAspect {

    private final static Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Pointcut("@annotation(com.efunds.log.annotations.ServiceLog)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {

    }

    /**
     * 在切点之后织入
     * @throws Throwable
     */
    @After("pointcut()")
    public void doAfter() throws Throwable {

    }

    /**
     * 环绕
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
       return null;
    }



}
