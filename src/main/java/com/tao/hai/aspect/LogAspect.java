package com.tao.hai.aspect;

import com.tao.hai.bean.Log;
import com.tao.hai.bean.User;
import com.tao.hai.service.LogService;
import com.tao.hai.util.HttpContextUtils;
import com.tao.hai.util.JSONUtils;
import com.tao.hai.util.NetworkUtils;
import com.tao.hai.util.ShiroUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
public class LogAspect {
    @Resource
    LogService logService;

    @Pointcut("@annotation(com.tao.hai.annotation.Log)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        /**执行开始时间*/
        long beginTime = System.currentTimeMillis();
        /**执行方法*/
        Object result = point.proceed();
        /**执行时长*/
        long time = System.currentTimeMillis() - beginTime;
        /**异步保存日志*/
        saveLog(point, time);
        return result;
    }

    /**
     * 异步保存日志
     */
    public void saveLog(ProceedingJoinPoint point, long time) throws InterruptedException {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        /**日志对象*/
        Log log = new Log();
        /**获取注解日志信息*/
        com.tao.hai.annotation.Log sysLog = method.getAnnotation(com.tao.hai.annotation.Log.class);
        if (sysLog != null) {
            log.setAction(sysLog.value());
        }
        /**请求的方法名*/
        String className = point.getTarget().getClass().getName();
        String methodName = method.getName();
        log.setMethod(className + "." + methodName + "()");
        /**请求的参数*/
        Object[] args = point.getArgs();
        try {
            String params = "";
            if (args != null) {
                for (Object object : args) {
                    if (object instanceof Model) {

                    } else if (object instanceof HttpServletRequest) {

                    } else if (object instanceof HttpServletResponse) {

                    } else {
                        params = params + "," + JSONUtils.toJsonString(object);
                    }
                }
            }
            log.setParams(params);
        } catch (Exception e) {

        }
        /**获取IP请求地址*/
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        log.setHost(NetworkUtils.getIpAddress(request));
        /**获取用户名*/
        User user = ShiroUtils.getUser();
        if (user != null) {
            log.setUserName(user.getLoginName());
        }
        // 系统当前时间
        log.setInsertTime(LocalDateTime.now());
        // 保存系统日志
        logService.save(log);
    }
}
