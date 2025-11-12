package com.amc.api.Controllers;

import com.amc.api.DTO.UserDTO;
import com.amc.api.Entities.User;
import com.amc.api.Services.AuthorizationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth/login")
public class AuthenticationController {

    private AuthorizationService authorizationService;

    @PostMapping
    public ResponseEntity<User> userAuthenticationLogin(@Valid @RequestBody UserDTO userData) {
        String userEmail = userData.getEmail();
        return Optional.ofNullable(authorizationService.loadUserByUsername(userEmail))
                .map(userDetails -> {
                    User user = (User) userDetails;
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

}
