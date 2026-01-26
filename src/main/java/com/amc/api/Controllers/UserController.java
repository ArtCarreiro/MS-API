package com.amc.api.Controllers;

import com.amc.api.DTO.UserDTO;
import com.amc.api.Entities.User;
import com.amc.api.Repositories.UserRepository;
import com.amc.api.Services.UserService;
import com.amc.api.Utils.Exceptions;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{uuid}")
    public ResponseEntity<User> getUserByUuid(@PathVariable("uuid") String userUuid ) {
        if (userRepository.findByUuid(userUuid) == null)
            throw new Exceptions.ResourceNotFoundException("Usuário não encontrado.");
        User user = userService.findUserByUuid(userUuid);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User newUser) {
        if (userRepository.findByEmail(newUser.getEmail()) != null)
            throw new Exceptions.DatabaseException("Já existe um usuário com o e-mail informado.");
        User user = userService.createUser(newUser);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<User> updateUser(@Valid @PathVariable("uuid") String userUuid, @RequestBody User userData) {
        if (userRepository.findByUuid(userUuid) == null)
            throw new Exceptions.ResourceNotFoundException("Usuário não encontrado.");
        User user = userService.updateUser(userUuid ,userData);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteUser(@PathVariable("uuid") String userUuid ) {
        if (userRepository.findByUuid(userUuid) == null)
            throw new Exceptions.ResourceNotFoundException("Usuário não encontrado.");
        boolean user = userService.deleteUser(userUuid);
        return user ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }
}
