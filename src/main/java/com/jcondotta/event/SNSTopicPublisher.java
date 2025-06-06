package com.jcondotta.event;

public interface SNSTopicPublisher<T> {
    String publishMessage(T messagePayload);
}
