package com.amc.api.Services;

import com.amc.api.Entities.Address;
import com.amc.api.Entities.User;
import com.amc.api.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

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

    @Transactional
    public User updateUser( String userUuid, User newUser) {
        modelMapper.typeMap(User.class, User.class)
                .addMappings(mapper -> mapper.skip(User::setUuid));
        try {
            if (newUser != null) {
                User newUserData = userRepository.findByUuid(userUuid);
                modelMapper.map(newUser, newUserData);
                return userRepository.save(newUserData);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Transactional
    public boolean deleteUser(String userUuid) {
        try {
            User user = userRepository.findByUuid(userUuid);
            userRepository.delete(user);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
