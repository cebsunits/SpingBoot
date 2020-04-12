package com.tao.hai.websocket.service;


public interface WebSocketService {
    /**
     * 向指定用户发送事件
     *
     * @param userName
     * @param destination
     * @param message
     */
    void convertAndSendToUser(String userName, String destination, Object message);

    /**
     * 通知指定用户success事件
     *
     * @param userName
     * @param message
     */
    void convertAndSendToUserSuccess(String userName, Object message);

    /**
     * 发送事件
     *
     * @param destination
     * @param message
     */
    void convertAndSend(String destination, Object message);

    /**
     * 发送success事件
     *
     * @param destination
     * @param message
     */
    void convertAndSendSuccess(String destination, Object message);
}
