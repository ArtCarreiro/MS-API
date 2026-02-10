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
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{uuid}")
    public ResponseEntity<User> getUserByUuid(@PathVariable("uuid") String uuid ) {
        return userRepository.findAll().stream()
                .filter(user -> uuid.equals(user.getUuid()))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> user = userRepository.findAll().stream()
                .filter(User::getActive)
                .toList();
        return user.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User newUser) {
        if (userRepository.findByEmail(newUser.getEmail()) != null)
            throw new Exceptions.DatabaseException("Já existe um usuário com o e-mail: " + newUser.getEmail() + ".");
        User user = userService.createUser(newUser);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<User> updateUser(@Valid @PathVariable("uuid") String userUuid, @RequestBody UserDTO newUserData) {
        if (userRepository.findByUuid(userUuid) == null)
            throw new Exceptions.ResourceNotFoundException("Usuário não encontrado.");
        User user = userService.updateUser(userUuid, newUserData);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteUser(@PathVariable("uuid") String userUuid ) {
        if (userRepository.findByUuid(userUuid) == null)
            throw new Exceptions.ResourceNotFoundException("Usuário não encontrado.");
        boolean user = userService.deleteUser(userUuid);
        return user ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{uuid}/password")
    public ResponseEntity<?> updateUserPassword(@PathVariable("uuid") String userUuid, @RequestBody String newPassword) {
        if (userRepository.findByUuid(userUuid) == null)
            throw new Exceptions.ResourceNotFoundException("Usuário não encontrado.");
        boolean user = userService.updateUserPassword(userUuid, newPassword);
        return user ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
