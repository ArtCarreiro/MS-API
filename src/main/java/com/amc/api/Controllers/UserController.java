package com.amc.api.Controllers;

import com.amc.api.Entities.User;
import com.amc.api.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{uuid}")
    public ResponseEntity<User> getUserByUuid(@PathVariable("uuid") String userUuid ) {
        User user = userService.findUserByUuid(userUuid);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User newUser) {
        User user = userService.createUser(newUser);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<User> updateUser(@PathVariable("uuid") String userUuid, @RequestBody User userData) {
        User user = userService.updateUser(userUuid ,userData);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<User> deleteUser(@PathVariable("uuid") String userUuid ) {
        User user = userService.deleteUser(userUuid);
        return user != null ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }















}
