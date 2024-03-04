package com.attendance.apigateway.controller;

import com.attendance.apigateway.model.UserSignupDTO;
import com.attendance.apigateway.service.UserService;
import com.attendance.apigateway.util.CommonResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;


@RestController
public class UserController implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping(path = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> userSignup(@Valid @RequestBody UserSignupDTO userSignupDTO) {
        LOGGER.info("Request Received for userSignup() ...");
        String result =  userService.signup(userSignupDTO);
        if(result.equalsIgnoreCase(HttpStatus.OK.name())){
            return new ResponseEntity<>(new CommonResponse("Signup Successful",
                    true,
                    null,
                    HttpStatus.CREATED.value()),
                    HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new CommonResponse("Signup process is not completed. Please contact administration support.",
                true,
                null,
                HttpStatus.EXPECTATION_FAILED.value()),
                HttpStatus.EXPECTATION_FAILED);
    }

    @PostMapping(path = "/signin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> userSignin(@RequestParam long userId,
                                                     @RequestParam String password) {
        LOGGER.info("Request Received for userSignin() ...");
        String result =  userService.signin(userId, password);
        if(result.equalsIgnoreCase(HttpStatus.OK.name())){
            return new ResponseEntity<>(new CommonResponse("Signin Successful",
                    true,
                    null,
                    HttpStatus.OK.value()),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(new CommonResponse("Sign-in process is not completed. Please contact administration support.",
                true,
                null,
                HttpStatus.EXPECTATION_FAILED.value()),
                HttpStatus.EXPECTATION_FAILED);
    }
}
