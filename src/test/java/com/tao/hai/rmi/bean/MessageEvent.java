package com.tao.hai.rmi.bean;

import com.tao.hai.rmi.domain.Subscriber;

import java.io.Serializable;

public class MessageEvent {
    private Subscriber subscriber;
    private Serializable message;
    private Serializable topic;
    public MessageEvent(Subscriber subscriber, Serializable message, Serializable topic) {
        this.subscriber=subscriber;
        this.message=message;
        this.topic=topic;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public Serializable getMessage() {
        return message;
    }

    public void setMessage(Serializable message) {
        this.message = message;
    }

    public Serializable getTopic() {
        return topic;
    }

    public void setTopic(Serializable topic) {
        this.topic = topic;
    }
}
