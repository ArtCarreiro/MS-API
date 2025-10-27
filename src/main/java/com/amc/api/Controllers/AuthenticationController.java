package com.amc.api.Controllers;


import com.amc.api.Entities.Administrator;
import com.amc.api.Services.AdministratorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    AdministratorService administratorService;

//    @PostMapping("/login")
//    ResponseEntity<Administrator> userLogin(@Valid Administrator user) {
//
//
//    }



}
