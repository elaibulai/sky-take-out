package com.sky.aspect;

import com.sky.annotation.AtuoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
public class AtuoFillAspect {
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AtuoFill)")
    public void autoFillPointCut(){}

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //获取操作类型
        MethodSignature signature=(MethodSignature) joinPoint.getSignature();
        AtuoFill atuoFill =signature.getMethod().getAnnotation(AtuoFill.class);
        OperationType operationType= atuoFill.value();
        //获取参数
        Object[] args= joinPoint.getArgs();
        if(args ==null||args.length==0){
            return;
        }
        Object entity=args[0];
        //赋值数据
        LocalDateTime now=LocalDateTime.now();
        Long id= BaseContext.getCurrentId();
        //赋值
        if(operationType==OperationType.INSERT){
            Method setCreateTime = entity.getClass().getMethod("setCreateTime", LocalDateTime.class);
            Method setUpdateTime = entity.getClass().getMethod("setUpdateTime", LocalDateTime.class);
            Method setCreateUser = entity.getClass().getMethod("setCreateUser", Long.class);
            Method setUpdateUser = entity.getClass().getMethod("setUpdateUser", Long.class);
            setCreateTime.invoke(entity,now);
            setUpdateTime.invoke(entity,now);
            setCreateUser.invoke(entity,id);
            setUpdateUser.invoke(entity,id);
        }else if(operationType==OperationType.UPDATE){
            Method setUpdateTime = entity.getClass().getMethod("setUpdateTime", LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getMethod("setUpdateUser", Long.class);
            setUpdateTime.invoke(entity,now);
            setUpdateUser.invoke(entity,id);
        }
    }
}
