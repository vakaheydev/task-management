package com.vaka.daily.service.domain;

import com.vaka.daily.domain.UserType;
import com.vaka.daily.service.abstraction.CommonService;

public interface UserTypeService extends CommonService<UserType> {
    UserType getByUniqueName(String name);

    UserType getDefaultUserType();
}
