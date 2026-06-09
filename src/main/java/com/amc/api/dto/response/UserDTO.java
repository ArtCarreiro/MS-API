package com.amc.api.dto.response;

import com.amc.api.enums.UserRoleEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String email;
    private UserRoleEnum role;
}
