package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.LoginRequest;
import org.example.dto.RegisterRequest;
import org.example.entity.Permission;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User register(RegisterRequest request) {
        if (findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        Role userRole = roleRepository.findById("USER")
                .orElseGet(() -> {
                    System.err.println("Warning: Role 'USER' not found in DB, assigning fallback role with READ permission");

                    Permission read = new Permission();
                    read.setName("READ");

                    Role fallbackRole = new Role();
                    fallbackRole.setName("USER");
                    fallbackRole.setPermissions(Set.of(read));

                    return fallbackRole;
                });

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(Set.of(userRole));

        return userRepository.save(user);
    }

    public String login(LoginRequest request) {
        User user = findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return "Login successful";
    }
}
