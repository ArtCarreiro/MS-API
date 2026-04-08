package com.amc.api.interfaces;

import com.amc.api.dto.UserDTO;
import com.amc.api.entities.User;

public interface UserBO {
    
    User createUser(User data);

    User updateUser( String uuid, UserDTO data);

    boolean deleteUser(String uuid);

    boolean updateUserPassword(String uuid, String newPassword);

    User validationUser (String uuid);
}
