package com.vaka.daily.service.domain;

import com.vaka.daily.domain.User;
import com.vaka.daily.domain.dto.UserDto;
import com.vaka.daily.service.abstraction.CommonService;

import java.util.List;

public interface UserService extends CommonService<User> {
    List<User> getByUserTypeName(String userTypeName);

    User getByUniqueName(String name);

    User createFromDTO(UserDto userDTO);

    User getByTgId(Long tgId);
}
