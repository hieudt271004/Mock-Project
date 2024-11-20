package com.hieudt79.user_service.client.feign;

import com.hieudt79.user_service.client.feign.dto.UserCreateDTO;
import com.hieudt79.user_service.client.feign.dto.UserRegisterDTO;
import com.hieudt79.user_service.client.feign.dto.UserStatusSetDTO;
import com.hieudt79.user_service.config.FeignConfig;
import com.hieudt79.user_service.dto.req.UserDtoReq;
import com.hieudt79.user_service.dto.res.ResponseData;
import com.hieudt79.user_service.utils.enums.UserStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "IDENTITY-CLIENT", url = "http://localhost:9001", configuration = FeignConfig.class)
public interface IdentityClient {

    @PostMapping(value = "/auth/add", consumes = "application/json")
    public ResponseData<UserCreateDTO> addUser(@Valid @RequestBody UserDtoReq userDtoReq);

    @PatchMapping(value = "/{userId}", consumes = "application/json")
    public ResponseData<UserStatusSetDTO> updateStatus(@PathVariable long userId, @RequestBody UserStatus status);

    @DeleteMapping(value = "/delete", consumes = "application/json")
    public ResponseData<String> deleteUser(@PathVariable long userId);
}
