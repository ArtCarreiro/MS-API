package com.amc.api.Controllers;

import com.amc.api.Entities.User;
import com.amc.api.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserService user;




    @GetMapping()
    public ResponseEntity<User> getUserByUuid(@RequestBody String userUuid ) {


        return null;
    }















}
