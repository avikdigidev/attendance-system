package com.attendance.apigateway.service;

import com.attendance.apigateway.model.UserEntity;
import com.attendance.apigateway.model.UserSignupDTO;

public interface UserService {

    String signup(UserSignupDTO userSignupDTO);

    String signin(long userId, String password);

    UserEntity loadUserByUsername(String userName);

    UserEntity findByUserName(String userName);
}
