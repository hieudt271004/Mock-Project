package com.hieudt79.user_service.controller;

import com.hieudt79.user_service.client.feign.dto.UserCreateDTO;
import com.hieudt79.user_service.client.feign.dto.UserRegisterDTO;
import com.hieudt79.user_service.client.feign.dto.UserStatusSetDTO;
import com.hieudt79.user_service.dto.req.UserDtoReq;
import com.hieudt79.user_service.dto.res.PageResponse;
import com.hieudt79.user_service.dto.res.ResponseData;
import com.hieudt79.user_service.dto.res.ResponseError;
import com.hieudt79.user_service.dto.res.UserRes;
import com.hieudt79.user_service.repository.UserRepo;
import com.hieudt79.user_service.service.UserExportService;
import com.hieudt79.user_service.service.UserService;
import com.hieudt79.user_service.utils.enums.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
@Slf4j
@RequiredArgsConstructor
@Tag(name = "User controller")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserExportService userExportService;

    private static final String API_KEY = "api-key";

    @Operation(method = "POST", summary = "Register new user", description = "Register new user")
    @PostMapping("/register")
    public ResponseData<Long> registerUser(@RequestBody UserRegisterDTO request,
                                           @RequestHeader("API-KEY") String apiKey) {
        if(!API_KEY.equals(apiKey)) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "api key not match");
        }
        try {
            long userId = userService.createUser(request);
            return new ResponseData<>(HttpStatus.CREATED.value(), "user register success", userId);
        } catch (Exception e) {
            return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), "user register error");
        }

    }


    @Operation(method = "POST", summary = "Add new user and send account to identity", description = "Send a request to create new user")
    @PostMapping("/add")
    public ResponseData<UserCreateDTO> addUser(@Valid @RequestBody UserDtoReq userDtoReq) {
        log.info("add user: {}", userDtoReq.getFullName());
        try {
            UserCreateDTO user = userService.createUser(userDtoReq);
            return new ResponseData<>(HttpStatus.CREATED.value(), "User added success", user);
        }
        catch (Exception e) {
            log.info("{}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Add user failed");
        }
    }

    @Operation(summary = "Update user", description = "Send a request to update user")
    @PutMapping("/{userId}")
    public ResponseData<Void> updateUser(@PathVariable long userId,
                             @Valid @RequestBody UserDtoReq userDtoReq) {
        log.info("update user with id = {}", userId);
        try {
            userService.updateUser(userDtoReq, userId);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "user update successful");
        } catch (Exception e) {
            log.info("{}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "update user failed");
        }
    }

    //update user account

    @Operation(summary = "Change status of user", description = "Send a request to change status of user")
    @PatchMapping("/{userId}")
    public ResponseData<UserStatusSetDTO> updateStatus(@PathVariable long userId, @RequestBody UserStatus status) {
        log.info("update status user = {}", userId);
        try {
            String username = userService.getUserById(userId).getEmail();
            UserStatusSetDTO userStatusSet = UserStatusSetDTO.builder().status(status).userName(username).build();
            userService.changeUserStatus(status, userId);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "update user status success", userStatusSet);
        } catch (Exception e) {
            log.info("{}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "update user status failed");
        }
    }

    @Operation(summary = "Delete user permanently", description = "Send a request to delete user permanently")
    @DeleteMapping("/{userId}")
    public ResponseData<String> deleteUser(@PathVariable long userId) {
        log.info("delete user id = {}", userId);
        try {
            String username = userService.getUserById(userId).getEmail();
            userService.deleteUser(userId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "delete user ", username);
        } catch (Exception e){
            log.info("{}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "delete user failed");
        }
    }

    @Operation(summary = "Get user detail", description = "Send a request to get user information")
    @GetMapping("{userId}")
    public ResponseData<UserRes> getUser(@PathVariable int userId) {
        try {
            UserRes user = userService.getUserById(userId);
            log.info("find user by id = {}", userId);
            return new ResponseData<>(HttpStatus.OK.value(), "find user", user);
        } catch(Exception e) {
            log.info("throw exception: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "can not find user");
        }
    }

    @Operation(summary = "Get list of users per pageNo", description = "Send a request to get user list by pageNo and pageSize")
    @GetMapping("/list")
    public ResponseData<PageResponse> getAllUsers(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                      @Min(10) @RequestParam(defaultValue = "20", required = false) int pageSize) {
        try {
            log.info("Request get user list, pageNo={}, pageSize={}", pageNo, pageSize);
            PageResponse<?> users = userService.getAllUser(pageNo, pageSize);
            return new ResponseData<>(HttpStatus.OK.value(), "get all users", users);
        } catch (Exception e) {
            log.error("{}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "can not get all");
        }
    }

    @Operation(summary = "Export user list to excel file", description = "Send a request to get user list")
    @GetMapping("export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<UserRes> listUsers = userService.getAll();
        UserExportService userExportService = new UserExportService(listUsers);

        userExportService.export(response);
    }


    //List.of(new User(), new User());
}
