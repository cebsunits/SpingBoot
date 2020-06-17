package com.tao.hai.listener;

import com.tao.hai.websocket.service.impl.WebSocketServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tao.hai.constants.SystemConstants.WEBSOCKET_LOGOUT_USER;

public class ShiroSessionListener implements SessionListener {
    @Autowired
    WebSocketServiceImpl webSocketService;

    public void onStart(Session session) {
        String userName = (String) session.getAttribute("userName");

    }

    public void onStop(Session session) {
        String userName = (String) session.getAttribute("userName");
        if (StringUtils.isNotEmpty(userName)) {
            webSocketService.convertAndSendToUser(userName, WEBSOCKET_LOGOUT_USER, true);
        }
    }

    /**
     * 会话过期触发
     */
    public void onExpiration(Session session) {
        String userName = (String) session.getAttribute("userName");
        if (StringUtils.isNotEmpty(userName)) {
            webSocketService.convertAndSendToUser(userName, WEBSOCKET_LOGOUT_USER, true);
        }
    }

}
