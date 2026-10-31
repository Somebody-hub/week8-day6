package com.example.week8_day6.projection;

import com.example.week8_day6.model.TaskPriority;

public interface PriorityCount {
    TaskPriority getPriority();
    long getTotal();
}
