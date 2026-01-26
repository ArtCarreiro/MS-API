package com.amc.api.DTO;

import com.amc.api.Enums.UserRoleEnum;

import lombok.Data;

@Data
public class UserLoginDTO {
    
    private String email;

    private UserRoleEnum role;

}
