package com.example.lucaandrei.picturerecipealignment.aop;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class NameValidatorAspect {

    @Around("execution(public * setName (..))")
    public void pointcutMethodArgument(ProceedingJoinPoint aJoinPoint) throws Throwable {
        Object[] args = aJoinPoint.getArgs();

        String name = (String) args[0];

        if (name.length()>1){
            aJoinPoint.proceed();
        }
        else {
            throw new RuntimeException("Invalid parameter");
        }
    }
}

