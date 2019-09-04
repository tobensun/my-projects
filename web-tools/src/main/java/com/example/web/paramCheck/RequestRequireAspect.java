package com.example.web.paramCheck;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class RequestRequireAspect {



    public RequestRequireAspect() {
//        log.info("初始化接口参数非空判断切面类...");
    }

    @Pointcut("@annotation(com.bonc.ioc.iot.annotation.RequestRequire)")
    public void controllerInteceptor() {
    }

    @Around("controllerInteceptor()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        //获取注解的方法参数列表
        Object[] args = pjp.getArgs();

        //获取被注解的方法
        MethodInvocationProceedingJoinPoint mjp = (MethodInvocationProceedingJoinPoint) pjp;
        MethodSignature signature = (MethodSignature) mjp.getSignature();
        Method method = signature.getMethod();

        //获取方法上的注解
        RequestRequire require = method.getAnnotation(RequestRequire.class);

        //以防万一，将中文的逗号替换成英文的逗号
        String fieldNames=require.require().replace("，", ",");

        //从参数列表中获取参数对象
        Object parameter=null;
        for(Object pa:args){
            //class相等表示是同一个对象
            if (pa.getClass()==require.parameter() ) {
                parameter=pa;
            }
        }

        //通过反射去和指定的属性值判断是否非空
        Class cl=parameter.getClass();
        for(String fieldName:fieldNames.split(",")){

            //根据属性名获取属性对象
//            Field f=cl.getDeclaredField(fieldName);

            Field f=ReflectUtil.getDeclaredFields(parameter,fieldName);

            //设置可读写权限
            f.setAccessible(true);


            //获取参数值，因为我的参数都是String型所以直接强转
            Object value=f.get(parameter);

            //非空判断
            if(value==null||!StringUtils.isNotNull(value.toString())){
                log.error("参数："+fieldName+"不允许为空");
                //将异常写会页面
                AppReply appReply=AppReply.error("参数："+fieldName+"不允许为空", ExceptionCodeUtil.IOCE_AS002);
                ConvertObject2Json.writeJson(appReply,HttpServletUtil.getHttpServletRequest(),HttpServletUtil.getHttpServletResponse());
                throw new IllegalArgumentException("参数"+fieldName+"不允许为空");
            }
        }

        //如果没有报错，放行
        return pjp.proceed();
    }
}
