package com.meetup.meetup.entity;


public enum EventPeriodicity {
    ONCE(6),
    HOUR(1),
    DAY(2),
    WEEK(3),
    MONTH(4),
    YEAR(5);

    private final int value;

    private EventPeriodicity(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
