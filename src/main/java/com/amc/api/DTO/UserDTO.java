package com.amc.api.DTO;

import com.amc.api.Enums.UserRoleEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String email;
    private UserRoleEnum role;
}
