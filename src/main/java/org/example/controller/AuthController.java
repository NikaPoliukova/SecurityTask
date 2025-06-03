package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.LoginRequest;
import org.example.dto.RegisterRequest;
import org.example.entity.User;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.register(request);
            return ResponseEntity.ok("User " + user.getEmail() + " successfully registered");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        try {
            String result = userService.login(request);
            return ResponseEntity.ok(result);
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
