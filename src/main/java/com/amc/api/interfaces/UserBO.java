package com.amc.api.interfaces;

import com.amc.api.dto.response.UserDTO;
import com.amc.api.entities.User;

public interface UserBO {
    
    User createUser(User data);

    User updateUser( String uuid, UserDTO data);

    boolean deleteUser(String uuid);
    
    void validateUser (User user);

    boolean updateUserPassword(String uuid, String newPassword);

}
