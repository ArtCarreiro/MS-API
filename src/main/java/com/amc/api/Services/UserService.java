package com.amc.api.Services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.amc.api.DTO.UserDTO;
import com.amc.api.Entities.User;
import com.amc.api.Enums.UserRoleEnum;
import com.amc.api.Interfaces.UserBO;
import com.amc.api.Repositories.UserRepository;
import com.amc.api.Utils.Exceptions;

import jakarta.transaction.Transactional;

@Service
public class UserService implements UserBO {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public User createUser(User user) {
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
    public User updateUser( String uuid, UserDTO data) {
        try {
            User user = userRepository.findByUuid(uuid);
            if(user == null)
                throw new RuntimeException("Usuário não encontrado");
            modelMapper.map(data, user);
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
    public User validationUser (String uuid) {
        User user = userRepository.findByUuid(uuid);
        if (user == null) 
            throw new Exceptions.ResourceNotFoundException("Usuário não encontrado.");
        if (user.getDeleted()) 
            throw new Exceptions.DatabaseException("Usuário deletado.");
        return user;
    }

}
