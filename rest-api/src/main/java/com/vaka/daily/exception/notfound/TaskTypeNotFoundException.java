package com.vaka.daily.exception.notfound;

public class TaskTypeNotFoundException extends ObjectNotFoundException {
    public TaskTypeNotFoundException(String detailName, Object detailValue) {
        super(detailName, detailValue);
    }

    @Override
    protected String getObjectName() {
        return "TaskType";
    }
}
