package com.example.week8_day6.projection;

import com.example.week8_day6.model.TaskStatus;

public interface StatusCount {
    TaskStatus getStatus();
    long getTotal();
}
