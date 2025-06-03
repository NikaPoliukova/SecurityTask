package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserDto;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        List<UserDto> userDtos = users.stream()
                .map(user -> new UserDto(
                        user.getId(),
                        user.getEmail(),
                        user.getRoles().stream()
                                .map(Role::getName)
                                .collect(Collectors.toSet())
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDtos);
    }
}
