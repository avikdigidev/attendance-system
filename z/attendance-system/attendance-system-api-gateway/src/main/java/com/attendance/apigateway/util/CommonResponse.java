package com.attendance.apigateway.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommonResponse implements Serializable {

    private String message;
    private boolean isValidRequest;
    private Object responseData;
    private Integer status;

}
