package com.attendance.apigateway.repository;

import com.attendance.apigateway.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserName(String userName);
    UserEntity findByUserNameAndStatus(String userName, String active);
    UserEntity findByUserIdAndPasswordAndStatus(long userId, String password, String active);
}