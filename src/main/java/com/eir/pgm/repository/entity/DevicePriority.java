package com.eir.pgm.repository.entity;

public enum DevicePriority {
    low(0), high(1);

    private Integer priority;

    private DevicePriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getPriority() {
        return this.priority;
    }
}

