package com.vaka.daily.exception.notfound;

public class ScheduleNotFoundException extends ObjectNotFoundException {
    public ScheduleNotFoundException(String detailName, Object detailValue) {
        super(detailName, detailValue);
    }

    @Override
    protected String getObjectName() {
        return "Schedule";
    }
}
