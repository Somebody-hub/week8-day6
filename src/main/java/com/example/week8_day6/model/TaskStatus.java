package com.example.week8_day6.model;

public enum TaskStatus {
    NEW("New"),
    IN_PROGRESS("In progress"),
    DONE("Done"),
    CANCELED("Canceled");

    private final String taskStatusTitle;

    TaskStatus(String taskStatusTitle) {
        this.taskStatusTitle = taskStatusTitle;
    }

    public boolean canTransitionTo(TaskStatus next) {
        if ((next == null) || (this == next)) {
            return false;
        }
        return switch (this) {
            case NEW -> (next == IN_PROGRESS || next == CANCELED);
            case IN_PROGRESS -> next == DONE || next == CANCELED;
            case DONE, CANCELED -> false;
        };
    }

    public String getTaskStatusTitle() {
        return taskStatusTitle;
    }
}
