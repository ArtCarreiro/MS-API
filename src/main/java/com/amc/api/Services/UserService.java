package com.amc.api.Services;

import com.amc.api.Entities.User;
import com.amc.api.Repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public User findUserByUuid(String userUuid){
        User userConsult = userRepository.findByUuid(userUuid);
        try {
            if (userConsult != null)
                return userConsult;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }


}
