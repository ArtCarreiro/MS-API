package com.amc.api.DTO;

import com.amc.api.Entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDTO extends User  {

    private String email;

    @JsonIgnore
    private String password;
}
