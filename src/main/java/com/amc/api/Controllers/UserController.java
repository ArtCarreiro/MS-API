package com.amc.api.Controllers;

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

import com.amc.api.DTO.UserDTO;
import com.amc.api.Entities.User;
import com.amc.api.Interfaces.UserBO;
import com.amc.api.Repositories.UserRepository;
import com.amc.api.Utils.Exceptions;

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
    public ResponseEntity<User> createUser(@Valid @RequestBody User newUser) {
        if (userRepository.findByEmail(newUser.getEmail()) != null)
            throw new Exceptions.DatabaseException("Já existe um usuário com o e-mail: " + newUser.getEmail() + ".");
        User user = userBO.createUser(newUser);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<User> updateUser(@Valid @PathVariable("uuid") String userUuid, @RequestBody UserDTO newUserData) {
        if (userRepository.findByUuid(userUuid) == null)
            throw new Exceptions.ResourceNotFoundException("Usuário não encontrado.");
        User user = userBO.updateUser(userUuid, newUserData);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteUser(@PathVariable("uuid") String userUuid ) {
        if (userRepository.findByUuid(userUuid) == null)
            throw new Exceptions.ResourceNotFoundException("Usuário não encontrado.");
        boolean user = userBO.deleteUser(userUuid);
        return user ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{uuid}/password")
    public ResponseEntity<?> updateUserPassword(@PathVariable("uuid") String userUuid, @RequestBody String newPassword) {
        if (userRepository.findByUuid(userUuid) == null)
            throw new Exceptions.ResourceNotFoundException("Usuário não encontrado.");
        boolean user = userBO.updateUserPassword(userUuid, newPassword);
        return user ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
