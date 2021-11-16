package com.zhy.log.aspect;

import com.zhy.log.util.DataUtil;
import com.google.gson.Gson;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
public class RequestLogAspect {

    private final static Logger logger = LoggerFactory.getLogger(RequestLogAspect.class);

    @Pointcut("@annotation(com.zhy.log.annotations.RequestLog)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        logger.info("-> request_Id     : {}", DataUtil.randomRequestId());
        // 打印请求的 IP
        logger.info("-> request_IP     : {}", request.getRemoteAddr());
        // 打印请求 url
        logger.info("-> URL            : {}", request.getRequestURL().toString());
        // 打印 Http method
        logger.info("-> Method         : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        // logger.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        // 打印请求入参
        logger.info("-> Request Param   : {}", new Gson().toJson(joinPoint.getArgs()));
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
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        // 打印出参
        logger.info("-> Response       : {}", new Gson().toJson(result));
        // 执行耗时
        logger.info("-> Time-Consuming : {} ms", System.currentTimeMillis() - startTime);
        return result;
    }


}
