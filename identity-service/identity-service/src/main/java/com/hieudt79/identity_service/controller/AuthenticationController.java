package com.hieudt79.identity_service.controller;

import com.hieudt79.identity_service.client.feign.UserClient;
import com.hieudt79.identity_service.client.feign.dto.UserCreateDTO;
import com.hieudt79.identity_service.client.feign.dto.UserRegisterDTO;
import com.hieudt79.identity_service.client.feign.dto.UserStatusSetDTO;
import com.hieudt79.identity_service.dto.req.RegisterRequest;
import com.hieudt79.identity_service.dto.req.ResetPasswordDTO;
import com.hieudt79.identity_service.dto.req.SignInRequest;
import com.hieudt79.identity_service.dto.res.ResponseData;
import com.hieudt79.identity_service.dto.res.ResponseError;
import com.hieudt79.identity_service.dto.res.TokenResponse;
import com.hieudt79.identity_service.model.Token;
import com.hieudt79.identity_service.service.AuthenticationService;
import com.hieudt79.identity_service.service.UserAccountService;
import feign.FeignException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Validated
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller")
public class AuthenticationController {

    private final UserClient userClient;
    private final AuthenticationService authenticationService;
    private final UserAccountService userAccountService;

    private static final String API_KEY = "api-key";


    //register user
    @Operation(method = "POST", summary = "Save user account and forward to user service ", description = "Send a request to register new user")
    @PostMapping("/register")
    public ResponseData<UserRegisterDTO> registerUser(@RequestBody RegisterRequest request)
            throws MessagingException, UnsupportedEncodingException {
        userAccountService.save(request);
        try {
            ResponseData<UserRegisterDTO> response = userClient.registerUser(request);
            return new ResponseData<>(HttpStatus.OK.value(), "User register", response.getData());
        } catch (FeignException e) {
            return new ResponseError(e.status(), "Register failed: " + e.getMessage());
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Register failed" + e.getMessage());
        }
    }

    //get user from user service and save new user to db
    @Operation(method = "POST", summary = "Get user from user service and save new user to db ", description = "Get new user")
    @PostMapping(value = "/add")
    public ResponseData<Void> addUser(@RequestBody UserCreateDTO userCreateDTO) {
        try {
            userAccountService.createUser(userCreateDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), "User added successfully");
        } catch(Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error: " + e.getMessage());
        }

    }


    //login - create token
    @Operation(method = "POST", summary = "Login", description = "Login")
    @PostMapping("/login")
    public ResponseData<TokenResponse> accessToken(@RequestBody SignInRequest request) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "User login", authenticationService.accessToken(request));
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Unexpected error: " + e.getMessage());
        }
    }


    //refresh token
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(HttpServletRequest request) {
        return new ResponseEntity<>(authenticationService.refreshToken(request), OK);
    }

    @Operation(summary = "Delete user permanently", description = "Handle deletion of user in the identity-service")
    @DeleteMapping("/delete")
    public ResponseData<Void> deleteUser(@RequestBody String username) {
        try {
            userAccountService.deleteByEmail(username);
            return new ResponseData<>(HttpStatus.OK.value(), "User deleted successfully");
        } catch (Exception e) {
            log.error("Failed to delete user in identity-service: {}", e.getMessage(), e);
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Failed to delete user in identity-service");
        }
    }

    @Operation(summary = "Change status of user", description = "Send a request to change status of user")
    @PatchMapping("/{userId}")
    public ResponseData<Void> updateStatus(@RequestBody UserStatusSetDTO userStatusSetDTO) {
        try {
            userAccountService.changeUserStatus(userStatusSetDTO.getUserName(), userStatusSetDTO.getStatus());
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "update user status success");
        } catch (Exception e) {
            log.info("{}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "update user status failed");
        }
    }


//

    //log out remove token
    @PostMapping("/log-out")
    public ResponseEntity<String> removeToken(HttpServletRequest request) {
        return new ResponseEntity<>(authenticationService.removeToken(request), OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody String email) {
        return new ResponseEntity<>(authenticationService.forgotPassword(email), OK);
    }

    @GetMapping("/reset-password")
    public ResponseEntity<String> resetPassword(HttpServletRequest request) {
        try {
            String secretKey = request.getHeader("X-Secret-Key");
            if (secretKey == null || secretKey.isBlank()) {
                return new ResponseEntity<>("Missing or invalid secret key", HttpStatus.BAD_REQUEST);
            }

            String response = authenticationService.resetPassword(secretKey);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error during password reset: ", e);
            return new ResponseEntity<>("Failed to reset password", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid ResetPasswordDTO request) {
        return new ResponseEntity<>(authenticationService.changePassword(request), OK);
    }
}
