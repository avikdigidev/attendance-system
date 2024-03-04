package com.attendance.apigateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserSignupDTO implements Serializable {

    private Long userId;
    @NotEmpty(message = "User name can't be null or empty")
    private String userName;
    @NotEmpty(message = "Must be provide password")
    @Size(min = 8, max = 32, message = "Password must be between 2 and 32 characters long")
    private String password;
    @NotEmpty(message = "Must be provide email")
    private String email;


}
