package com.example.week8_day6.exception;

import com.example.week8_day6.model.TaskStatus;

public class StatusTransitionException extends RuntimeException {
    public StatusTransitionException(TaskStatus from, TaskStatus to) {
        super("Cannot change task status from " + from + " to " + to);
    }
}