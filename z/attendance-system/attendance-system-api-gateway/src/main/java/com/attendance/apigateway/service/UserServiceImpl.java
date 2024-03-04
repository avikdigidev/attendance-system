package com.attendance.apigateway.service;

import com.attendance.apigateway.model.UserEntity;
import com.attendance.apigateway.model.UserSignupDTO;
import com.attendance.apigateway.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserEntity loadUserByUsername(String userName) {
        LOGGER.info("loadUserByUsername() method call...");
        boolean isAccountNonLocked = false;
        UserEntity userEntity = userRepository.findByUserNameAndStatus(userName, "ACTIVE");
        if (userEntity == null) {
            LOGGER.error("User not found or user deactivated.");
            throw new RuntimeException("User not found or account deactivated.");
        }
        return userEntity;
    }

    @Override
    public String signin(long userId, String password) {
        LOGGER.info("signin() method call...");
        try {
            UserEntity userEntity = userRepository.findByUserIdAndPasswordAndStatus(userId, password, "ACTIVE");
            if (userEntity == null) {
                LOGGER.error("User not found or user deactivated.");
                throw new RuntimeException("User not found or account deactivated.");
            } else {
                return HttpStatus.OK.name();
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return HttpStatus.EXPECTATION_FAILED.name();
        }
    }

    @Override
    public String signup(UserSignupDTO userSignupDTO) {
        LOGGER.info("signup() method call...");
        try {

            //Finally User Signup
            UserEntity userEntity = new UserEntity();
            userEntity.setUserId(userSignupDTO.getUserId());
            userEntity.setUserName(userSignupDTO.getUserName());
            userEntity.setPassword(userSignupDTO.getPassword());
            userEntity.setCreateDate(LocalDateTime.now());
            userEntity.setStatus("ACTIVE");
            userEntity.setEmailId(userSignupDTO.getEmail());
            UserEntity userAfterSave = userRepository.save(userEntity);
            if(userAfterSave !=null && userAfterSave.getUserId() !=null){
                return HttpStatus.OK.name();
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return HttpStatus.EXPECTATION_FAILED.name();
        }
        return null;
    }

    @Override
    public UserEntity findByUserName(String userName) {
        LOGGER.info("signup() method call...");
        return userRepository.findByUserName(userName);
    }
}
