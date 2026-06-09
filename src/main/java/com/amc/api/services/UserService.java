package com.amc.api.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.amc.api.dto.response.UserDTO;
import com.amc.api.entities.User;
import com.amc.api.enums.UserRoleEnum;
import com.amc.api.interfaces.UserBO;
import com.amc.api.repositories.UserRepository;
import com.amc.api.utils.Exceptions;

import jakarta.transaction.Transactional;

@Service
public class UserService implements UserBO {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public User createUser(User user) {
        validateUser(user);
        try {
            String passwordEncoded = passwordEncoder.encode(user.getPassword());
            user.setPassword(passwordEncoded);
            user.setRole(UserRoleEnum.valueOf(user.getRole().toString()));
            return userRepository.save(user);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public User updateUser(String uuid, UserDTO data) {
    User user = userRepository.findByUuid(uuid);
    validateUser(user);
        try {
            mapper.map(data, user);
            userRepository.save(user);
            return user;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateUserPassword(String uuid, String newPassword) {
        try {
            User user = userRepository.findByUuid(uuid);
            String passwordEncoded = passwordEncoder.encode(newPassword);
            user.setPassword(passwordEncoded);
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public boolean deleteUser(String uuid) {
        try {
            userRepository.deleteUserByUuid(uuid);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void validateUser (User user) {
        if (userRepository.findByUuid(user.getUuid()) == null) 
            throw new Exceptions.ResourceNotFoundException("Usuário não encontrado.");
        if (userRepository.findByEmail(user.getEmail()) != null) 
            throw new Exceptions.DatabaseException("Já existe um usuário com o e-mail: " + user.getEmail() + ".");
    }
}
