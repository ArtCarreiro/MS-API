package com.amc.api.Services;

import com.amc.api.Entities.User;
import com.amc.api.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUserByUuid(String userUuid) {
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

    public User updateUser( String userUuid, User user) {
        try {
            User newUserData = userRepository.findByUuid(userUuid);
            if (user != null) {
                newUserData.setEmail(user.getEmail());
                newUserData.setKeyword(user.getKeyword());
                newUserData.setPhone(user.getPhone());
                newUserData.setFirstName(user.getFirstName());
                newUserData.setLastName(user.getLastName());
                newUserData.setProfession(user.getProfession());
                userRepository.save(newUserData);
                return newUserData;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Transactional
    public boolean deleteUser(String userUuid) {
        try {
            User userDeleted = userRepository.findByUuid(userUuid);
            userRepository.delete(userDeleted);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
