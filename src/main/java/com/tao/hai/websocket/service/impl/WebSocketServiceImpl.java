package com.tao.hai.websocket.service.impl;

import com.tao.hai.json.AjaxSuccess;
import com.tao.hai.websocket.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import static com.tao.hai.constants.SystemConstants.WEBSOCKET_MESSAGE_ALL;
import static com.tao.hai.constants.SystemConstants.WEBSOCKET_MESSAGE_USER;

@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    /**
     * 向指定用户发送事件
     *
     * @param userName
     * @param destination
     * @param message
     */
    public void convertAndSendToUser(String userName, String destination, Object message) {
        simpMessagingTemplate.convertAndSendToUser(userName,destination,message);
    }

    /**
     * 通知所有用户success事件
     *
     * @param message
     */
    public void convertAndSendToUserSuccess(String userName, Object message) {
        AjaxSuccess ajaxSuccess=new AjaxSuccess();
        ajaxSuccess.setData(message);
        simpMessagingTemplate.convertAndSendToUser(userName,WEBSOCKET_MESSAGE_USER,ajaxSuccess);
    }



    /**
     * 发送事件
     *
     * @param destination
     * @param message
     */
    public void convertAndSend(String destination, Object message) {
        simpMessagingTemplate.convertAndSend(destination,message);
    }

    /**
     * 发送success事件
     *
     * @param destination
     * @param message
     */
    public void convertAndSendSuccess(String destination, Object message) {
        AjaxSuccess ajaxSuccess=new AjaxSuccess();
        ajaxSuccess.setData(message);
        simpMessagingTemplate.convertAndSend(WEBSOCKET_MESSAGE_ALL,ajaxSuccess);
    }
}
