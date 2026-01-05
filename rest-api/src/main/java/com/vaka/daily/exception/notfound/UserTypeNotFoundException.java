package com.vaka.daily.exception.notfound;

public class UserTypeNotFoundException extends ObjectNotFoundException {
    public UserTypeNotFoundException(String detailName, Object detailValue) {
        super(detailName, detailValue);
    }

    @Override
    protected String getObjectName() {
        return "UserType";
    }
}
