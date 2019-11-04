package com.example.intercept;


import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @AfterReturning(value = "within(ltd.pdx.freehomerest.controller..*)",returning = "rvt")
    public void after(Object rvt){
        log.info("接口返回:{}",new Gson().toJson(rvt));
    }
    @Before("execution(* ltd.pdx.freehomerest.controller..*(..))")
    public void before(JoinPoint point){
        Object[] args = point.getArgs();
        log.info("请求参数:{}", Arrays.toString(args));
    }
}
