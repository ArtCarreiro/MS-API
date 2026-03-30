package com.amc.api.Interfaces;

import com.amc.api.DTO.UserDTO;
import com.amc.api.Entities.User;

public interface UserBO {
    
    User findUserByUuid(String userUuid);

    User createUser(User newUser);

    User updateUser( String userUuid, UserDTO newUserData);

    boolean deleteUser(String userUuid);

    boolean updateUserPassword(String userUuid, String newPassword);

    User validationUser (String userUuid);
}
