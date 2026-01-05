package com.vaka.daily.exception.notfound;

public class BindingTokenNotFoundException extends ObjectNotFoundException {
    public BindingTokenNotFoundException(String detailName, Object detailValue) {
        super(detailName, detailValue);
    }

    @Override
    protected String getObjectName() {
        return "BindingToken";
    }
}
