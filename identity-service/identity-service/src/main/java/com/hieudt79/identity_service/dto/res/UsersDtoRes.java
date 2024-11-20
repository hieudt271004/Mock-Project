package com.hieudt79.identity_service.dto.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsersDtoRes {
    private String email;
    private String password;
}
