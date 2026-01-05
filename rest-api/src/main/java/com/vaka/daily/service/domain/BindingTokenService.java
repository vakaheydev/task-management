package com.vaka.daily.service.domain;

import com.vaka.daily.domain.BindingToken;

import java.util.List;

public interface BindingTokenService {
    List<BindingToken> getAll();
    BindingToken createToken(Integer userId);
    BindingToken getByTokenValue(String tokenValue);
    BindingToken getByUserId(Integer userId);
    BindingToken getById(Integer id);
    long count();
    int deleteExpiredTasks();
}
