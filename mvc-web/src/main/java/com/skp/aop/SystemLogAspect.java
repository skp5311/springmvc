package com.skp.aop;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;

/**  
 * 切点类  
 * @since 2014-08-05 Pm 20:35  
 * @version 1.0  
 */
@Aspect
@Component
public class SystemLogAspect {
    //本地异常日志记录对象    
    //private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);
    private static final Logger logger = LoggerFactory.getLogger("spring-aop");

    @Pointcut("execution(public * com.skp.web.controller..*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    @Order(1)
    public void doBefore(JoinPoint joinPoint) throws Throwable {

    }

    /** 
    * Around 手动控制调用核心业务逻辑，以及调用前和调用后的处理, 
    * 注意：当核心业务抛异常后，立即退出，转向AfterAdvice 执行完AfterAdvice，再转到ThrowingAdvice 
    */

    @Around(value = "webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Long startTime = System.currentTimeMillis();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        Object result = null;
        //环绕通知处理方法  
        result = joinPoint.proceed();
        //if (!method.toLowerCase().equals("get")) {
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        String requestBody = Arrays.toString(joinPoint.getArgs());
        LogEntity logMsg = new LogEntity(request.getRemoteAddr(), startTime, url, classMethod, requestBody);
        int runtime = (int) (System.currentTimeMillis() - startTime);
        logMsg.setRuntime(runtime);
        logMsg.setResponseBody(JSONObject.toJSONString(result));
        logger.info("after: " + logMsg.toString());
        //}

        return result;
    }

    /**
     * AfterReturning 核心业务逻辑调用正常退出后，不管是否有返回值，正常退出后，均执行此Advice
     * */
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {

    }

    /** 
     * After 核心业务逻辑退出后（包括正常执行结束和异常退出），执行此Advice 
     */
    @After(value = "webLog()")
    public void doAfter(JoinPoint joinPoint) throws Throwable {

    }
}
