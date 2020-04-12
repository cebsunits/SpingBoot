package com.tao.hai.facotry;

import com.tao.hai.bean.Log;
import com.tao.hai.util.NetworkUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.UUID;

public class LogFactory {

    public static Log createSysLog(String action, String event) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Log log = new Log();
        log.setLogId(UUID.randomUUID().toString());
        log.setAction(action);
        log.setMethod(event);
        log.setHost(NetworkUtils.getIpAddress(request));
        log.setUserName((String) request.getSession().getAttribute("userName"));
        log.setInsertTime(LocalDateTime.now());
        return log;
    }
}
