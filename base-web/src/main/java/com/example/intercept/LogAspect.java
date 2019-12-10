package com.example.intercept;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @AfterReturning(value = "within(com.hw8.controller..*)", returning = "rvt")
    public void after(Object rvt) {
        log.info("接口返回:{}", JSON.toJSON(rvt));
    }

    @Before("execution(* com.hw8.controller..*(..))")
    public void before(JoinPoint point) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Object[] args = point.getArgs();
        log.info("url={}", request.getRequestURL());
        log.info("请求参数:{}", Arrays.toString(args));
    }
}
