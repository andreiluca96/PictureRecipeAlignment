package com.example.lucaandrei.picturerecipealignment.aop;

import android.util.Log;
import android.view.View;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class TimeMonitorAspect {
    public long startedTime = 0;
    public static String TAG = "Time-ASPECT";

    @Pointcut("execution(void *.onClick*(..))")
    public void onButtonClick() {}

    @Before("onButtonClick() && args(view)")
    public void beforeButton(View view) {
        startedTime = System.currentTimeMillis();
    }

    @After("onButtonClick() && args(view)")
    public void afterButton(View view) {
        Log.i(TAG, String.format("Execution took <%s> milliseconds.", System.currentTimeMillis() - startedTime));
    }
}
