package com.amc.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amc.api.dto.UserDTO;
import com.amc.api.entities.User;
import com.amc.api.interfaces.UserBO;
import com.amc.api.repositories.UserRepository;
import com.amc.api.utils.Exceptions;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserBO userBO;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<User>> getActiveUsers() {
        List<User> users = userRepository.findAll()
                .stream()
                .filter(User::getActive)
                .toList();
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<User> getUserByUuid(@PathVariable String uuid) {
        return userRepository.findAll().stream()
                .filter(user -> uuid.equals(user.getUuid()))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User data) {
        if (userRepository.findByEmail(data.getEmail()) != null)
            throw new Exceptions.DatabaseException("Já existe um usuário com o e-mail: " + data.getEmail() + ".");
        User user = userBO.createUser(data);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<User> updateUser(@Valid @PathVariable String uuid, @RequestBody UserDTO data) {
        if (userRepository.findByUuid(uuid) == null)
            throw new Exceptions.ResourceNotFoundException("Usuário não encontrado.");
        User user = userBO.updateUser(uuid, data);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteUser(@PathVariable String uuid ) {
        if (userRepository.findByUuid(uuid) == null)
            throw new Exceptions.ResourceNotFoundException("Usuário não encontrado.");
        return userBO.deleteUser(uuid) == true ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{uuid}/password")
    public ResponseEntity<?> updateUserPassword(@PathVariable("uuid") String uuid, @RequestBody String newPassword) {
        if (userRepository.findByUuid(uuid) == null)
            throw new Exceptions.ResourceNotFoundException("Usuário não encontrado.");
        boolean user = userBO.updateUserPassword(uuid, newPassword);
        return user ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
