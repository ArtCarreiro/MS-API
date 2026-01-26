package com.amc.api.Services;

import com.amc.api.DTO.UserLoginDTO;
import com.amc.api.Utils.Exceptions;
import com.amc.api.Entities.User;
import com.amc.api.Enums.UserRoleEnum;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findUserByUuid(String userUuid) {
        try {
            return validationUser(userUuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User createUser(User newUser) {
        try {
            String passwordEncoded = passwordEncoder.encode(newUser.getPassword());
            newUser.setPassword(passwordEncoded);
            newUser.setRole(UserRoleEnum.valueOf(newUser.getRole().toString()));
            return userRepository.save(newUser);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public User updateUser( String userUuid, UserLoginDTO newUserData) {
        try {
            User user = userRepository.findByUuid(userUuid);
            modelMapper.map(newUserData, user);
            userRepository.save(user);
            return user;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    public User validationUser (String userUuid) {
        User user = userRepository.findByUuid(userUuid);
        if (user == null) 
            throw new Exceptions.ResourceNotFoundException("Usuário não encontrado.");
        if (user.getDeleted()) 
            throw new Exceptions.DatabaseException("Usuário deletado.");
        return user;
    }

}
