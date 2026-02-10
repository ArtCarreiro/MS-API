package com.amc.api.DTO;

import com.amc.api.Enums.UserRoleEnum;

import lombok.Data;

@Data
public class UserDTO {
    
    private String email;

    private UserRoleEnum role;

}
