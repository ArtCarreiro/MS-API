package com.amc.api.Services;

import com.amc.api.Entities.User;
import com.amc.api.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUserByUuid(String userUuid){
        try {
            return userRepository.findByUuid(userUuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User createUser(User newUser) {
        try {
            return userRepository.save(newUser);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }


}
