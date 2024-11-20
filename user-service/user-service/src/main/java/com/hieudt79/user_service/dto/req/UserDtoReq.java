package com.hieudt79.user_service.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hieudt79.user_service.dto.validator.EnumPattern;
import com.hieudt79.user_service.dto.validator.GenderSubset;
import com.hieudt79.user_service.dto.validator.PhoneNumber;
import com.hieudt79.user_service.utils.enums.Gender;
import com.hieudt79.user_service.utils.enums.UserRole;
import com.hieudt79.user_service.utils.enums.UserStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
@Valid
@Data
public class UserDtoReq implements Serializable{

    @NotBlank(message = "full name must be not blank")
    private String fullName;

    @GenderSubset(anyOf = {Gender.MALE, Gender.FEMALE, Gender.OTHER})
    //@NotBlank(message = "gender can not be null")
    private Gender gender;

    //@NotBlank(message = "date of birth can not be null")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate bod;

    private String password;
    private UserRole role;

    //@Pattern(regexp = "^\\d{10}$", message = "phone invalid format")
    @PhoneNumber(message = "phone invalid format")
    @NotBlank(message = "phone can not be null")
    private String phone;

    @Email(message = "invalid email")
    @NotBlank(message = "email can not be invalid")
    private String email;

    //@NotBlank(message = "status can not be invalid")
    //@Pattern(regexp = "^ACTIVE|INACTIVE|NONE$", message = "status must be one in {ACTIVE INACTIVE NONE}")
    @EnumPattern(name = "status", regexp = "ACTIVE|INACTIVE|NONE")
    private UserStatus status;
}
