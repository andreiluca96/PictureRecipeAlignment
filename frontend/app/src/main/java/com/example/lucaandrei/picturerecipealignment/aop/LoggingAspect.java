package com.example.lucaandrei.picturerecipealignment.aop;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
class LoggingAspect {
    public static String TAG = "LOGGING-ASPECT";

    @Pointcut("execution(void *.onButton*Click(..))")
    public void onButtonClick() {}

    @Before("onButtonClick() && args(view)")
    public void beforeButton(View view) {
        String text = null;
        if (view instanceof TextView) {
            text = ((TextView) view).getText().toString();
        }
        Activity host = (Activity) view.getContext();
        Log.i(TAG, String.format("Before [%s-%s] button click", host.getClass().getSimpleName(), text));
    }

    @After("onButtonClick() && args(view)")
    public void afterButton(View view) {
        String text = null;
        if (view instanceof TextView) {
            text = ((TextView) view).getText().toString();
        }
        Activity host = (Activity) view.getContext();
        Log.i(TAG, String.format("After [%s-%s] button click", host.getClass().getSimpleName(), text));
    }
}
