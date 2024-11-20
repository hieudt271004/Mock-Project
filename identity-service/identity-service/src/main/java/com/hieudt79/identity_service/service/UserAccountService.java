package com.hieudt79.identity_service.service;

import com.hieudt79.identity_service.client.feign.dto.UserCreateDTO;
import com.hieudt79.identity_service.client.feign.dto.UserRegisterDTO;
import com.hieudt79.identity_service.dto.req.RegisterRequest;
import com.hieudt79.identity_service.dto.req.SignInRequest;
import com.hieudt79.identity_service.model.UserAccount;
import com.hieudt79.identity_service.utils.enums.UserStatus;
import jakarta.mail.MessagingException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserAccountService {

    UserDetailsService userDetailService();

    List<String> getAllRoleByEmail(long userId);

    boolean emailExist(String email);

    UserAccount getByEmail(String email);

    void changeUserStatus(String username, UserStatus status);
    void verifyAccount(String email);

    void save(UserAccount userAccount);

    void save(RegisterRequest request);

    void registerUser(UserAccount userAccount);

    void updateLastLogin(SignInRequest request);

    void createUser(UserCreateDTO userCreateDTO);

    void deleteByEmail(String email);
}
