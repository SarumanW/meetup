package com.meetup.meetup.entity;


public enum EventType {
    EVENT(1),
    NOTE(2),
    PRIVATE_EVENT(3);

    private final int value;

    private EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
