package com.hieudt79.user_service.service.impl;

import com.hieudt79.user_service.client.feign.dto.UserCreateDTO;
import com.hieudt79.user_service.client.feign.dto.UserRegisterDTO;
import com.hieudt79.user_service.model.User;
import com.hieudt79.user_service.dto.req.UserDtoReq;
import com.hieudt79.user_service.dto.res.PageResponse;
import com.hieudt79.user_service.dto.res.UserRes;
import com.hieudt79.user_service.repository.UserRepo;
import com.hieudt79.user_service.service.UserService;
import com.hieudt79.user_service.utils.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PageResponse getAllUser(int pageNo, int pageSize) {
        Page<User> page = userRepo.findAll(PageRequest.of(pageNo, pageSize));

        List<UserRes> list = page.stream().map(user -> UserRes.builder().
                        userId(user.getUserId()).
                        fullName(user.getFullName()).
                        gender(user.getGender()).
                        bod(user.getBod()).
                        phone(user.getPhone()).
                        email(user.getEmail()).
                        status(user.getStatus()).
                        createdAt(user.getCreatedAt()).
                        updatedAt(user.getCreatedAt()).
                        build()
        ).toList();
        log.info("repo: paging all user");
        return PageResponse.builder().
                pageNo(pageNo).
                pageSize(pageSize).
                item(list).
                totalPage(page.getTotalPages()).
                build();
    }

    @Override
    public void updateUser(UserDtoReq userDtoReq, long userId) {
        User user = userRepo.findById(userId);
        user.setFullName(userDtoReq.getFullName());
        user.setBod(userDtoReq.getBod());
        user.setGender(userDtoReq.getGender());
        user.setPhone(userDtoReq.getPhone());
        user.setEmail(userDtoReq.getEmail());
        user.setStatus(userDtoReq.getStatus());

        userRepo.save(user);
        log.info("repo: user has update successfully user id = {}", userId);
    }

    @Override
    public UserDetailsService userDetailService() {
        return username -> userRepo.findByEmail(username);
    }

    //send user to identity service
    @Override
    public UserCreateDTO createUser(UserDtoReq userDtoReq) {
        String password = passwordEncoder.encode(userDtoReq.getPassword());
        String email = userDtoReq.getEmail();
        if(userRepo.findByEmail(email) != null) {
            throw new RuntimeException("Email already exists");
        }

        //create new user create dto
        UserCreateDTO userCreate = UserCreateDTO.builder().email(userDtoReq.getEmail()).password(password).
        role(userDtoReq.getRole()).status(userDtoReq.getStatus()).build();

        //create new user
        User user = new User();
        user.setFullName(userDtoReq.getFullName());
        user.setBod(userDtoReq.getBod());
        user.setGender(userDtoReq.getGender());
        user.setPhone(userDtoReq.getPhone());
        user.setEmail(userDtoReq.getEmail());
        user.setStatus(UserStatus.ACTIVE);

        //save user to db
        userRepo.save(user);
        return userCreate;
    }

    @Override
    public UserRes getUserById(long userId) {
        User user = userRepo.findById(userId);
        log.info("repo: find user with id = {}", userId);
        return UserRes.builder().
                userId(user.getUserId()).
                fullName(user.getFullName()).
                bod(user.getBod()).
                gender(user.getGender()).
                email(user.getEmail()).
                phone(user.getPhone()).
                status(user.getStatus()).
                createdAt(user.getCreatedAt()).
                updatedAt(user.getUpdatedAt()).
                build();

    }

    @Override
    public UserRes getUserByUsername(String username) {
        User user = userRepo.findByFullName(username);
        log.info("repo: Find user with username = {}", username);
        return UserRes.builder().
                userId(user.getUserId()).
                fullName(user.getFullName()).
                bod(user.getBod()).
                gender(user.getGender()).
                email(user.getEmail()).
                phone(user.getPhone()).
                status(user.getStatus()).
                createdAt(user.getCreatedAt()).
                updatedAt(user.getUpdatedAt()).
                build();
    }

    @Override
    public void deleteUser(long userId) {
        userRepo.deleteById(userId);
        log.info("repo: delete user with id = {}", userId);
    }

    @Override
    public List<UserRes> getAll() {
        List<User> users = userRepo.findAll();
        List<UserRes> userResList = users.stream().map(user -> UserRes.builder().userId(user.getUserId()).
                                                               fullName(user.getFullName()).
                                                               gender(user.getGender()).
                                                               bod(user.getBod()).
                                                               phone(user.getPhone()).
                                                               email(user.getEmail()).
                                                               status(user.getStatus()).build()).
                                                               toList();
        log.info("repo: get all user");
        return userResList;
    }

    @Override
    public long createUser(UserRegisterDTO userRegisterDTO) {
        User user = User.builder().fullName(userRegisterDTO.getFullName()).
                               email(userRegisterDTO.getEmail()).
                bod(userRegisterDTO.getBod()).
                gender(userRegisterDTO.getGender()).
                phone(userRegisterDTO.getPhone()).
                status(userRegisterDTO.getStatus()).build();
        userRepo.save(user);
        return user.getUserId();
    }

    @Override
    public void changeUserStatus(UserStatus status, long userId) {
        User user = userRepo.findById(userId);
        user.setStatus(status);
        userRepo.save(user);
        log.info("repo: change user status id = {}", userId);
    }

}
