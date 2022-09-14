package com.gyfz.service;

import com.gyfz.domain.ResponseResult;
import com.gyfz.domain.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
