package com.tao.hai.websocket.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${webSocket.webSocketPath}")
    private String webSocketPath;
    @Value("${webSocket.topicPath}")
    private String topicPath;
    @Value("${webSocket.userPath}")
    private String userPath;
    @Value("${webSocket.userDestinationPrefix}")
    private String userDestinationPrefix;
    @Value("${webSocket.applicationDestinationPrefixes}")
    private String applicationDestinationPrefixes;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(webSocketPath)//端点名称
                .setAllowedOriginPatterns("*")//跨域
                .withSockJS(); //使用sockJS
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //这里注册两个，主要是目的是将广播和队列分开。
        registry.enableSimpleBroker(topicPath, userPath);
        //定义websoket前缀
        registry.setApplicationDestinationPrefixes(applicationDestinationPrefixes);
        //定义一对一推送的时候前缀
        registry.setUserDestinationPrefix(userDestinationPrefix);
    }
}
