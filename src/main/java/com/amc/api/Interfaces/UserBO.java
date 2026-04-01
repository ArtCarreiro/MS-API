package com.amc.api.Interfaces;

import com.amc.api.DTO.UserDTO;
import com.amc.api.Entities.User;

public interface UserBO {
    
    User createUser(User data);

    User updateUser( String uuid, UserDTO data);

    boolean deleteUser(String uuid);

    boolean updateUserPassword(String uuid, String newPassword);

    User validationUser (String uuid);
}
