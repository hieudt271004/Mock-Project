package com.hieudt79.identity_service.dto.res;

import lombok.Builder;

import java.util.Date;
@Builder
public class RegisterResponse {
    private String fullName;
    private String gender;
    private String email;
    private String password;
    private Date bod;
    private String phone;
    private Date lastLogin;
    private String status;
}
