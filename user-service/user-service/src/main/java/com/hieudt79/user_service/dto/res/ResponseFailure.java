package com.hieudt79.user_service.dto.res;

import org.springframework.http.HttpStatus;

public class ResponseFailure extends ResponseSuccess{
    public ResponseFailure(HttpStatus status, String message) {
        super(status, message);
    }
}
