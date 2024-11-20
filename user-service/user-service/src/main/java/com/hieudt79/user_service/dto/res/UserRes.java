package com.hieudt79.user_service.dto.res;

import com.hieudt79.user_service.utils.enums.Gender;
import com.hieudt79.user_service.utils.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
public class UserRes {

    private long userId;

    private String fullName;

    private Gender gender;

    private LocalDate bod;

    private String phone;

    private String email;

    private UserStatus status;

    private LocalDate createdAt;

    private LocalDate updatedAt;
}
