package com.vaka.daily.exception.notfound;

public class TaskNotificationNotFoundException extends ObjectNotFoundException {
    public TaskNotificationNotFoundException(String detailName, Object detailValue) {
        super(detailName, detailValue);
    }

    @Override
    protected String getObjectName() {
        return "TaskNotification";
    }
}
