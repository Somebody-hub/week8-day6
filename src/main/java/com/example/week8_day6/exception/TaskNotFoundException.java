package com.example.week8_day6.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long message) {
        super(message.toString());
    }
}
