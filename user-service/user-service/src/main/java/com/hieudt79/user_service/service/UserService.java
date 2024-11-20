package com.hieudt79.user_service.service;

import com.hieudt79.user_service.client.feign.dto.UserCreateDTO;
import com.hieudt79.user_service.client.feign.dto.UserRegisterDTO;
import com.hieudt79.user_service.dto.req.UserDtoReq;
import com.hieudt79.user_service.dto.res.PageResponse;
import com.hieudt79.user_service.dto.res.UserRes;
import com.hieudt79.user_service.utils.enums.UserStatus;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {

    UserDetailsService userDetailService();

    UserCreateDTO createUser(UserDtoReq userDtoReq);

    void updateUser(UserDtoReq userDtoReq, long userId);

    void changeUserStatus(UserStatus status, long userId);

    UserRes getUserById(long userId);

    UserRes getUserByUsername(String username);

    PageResponse getAllUser(int pageNo, int pageSize);

    void deleteUser(long userId);

    List<UserRes> getAll();

    long createUser(UserRegisterDTO userRegisterDTO);

}
