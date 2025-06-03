package org.example.config;

import org.example.entity.Permission;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.repository.PermissionRepository;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initRolesAndPermissions(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository) {

        return args -> {

            Permission read = new Permission();
            read.setName("READ");

            Permission write = new Permission();
            write.setName("WRITE");

            Permission delete = new Permission();
            delete.setName("DELETE");

            permissionRepository.saveAll(List.of(read, write, delete));

            Role user = new Role();
            user.setName("USER");
            user.setPermissions(Set.of(read));
            roleRepository.save(user);

            Role admin = new Role();
            admin.setName("ADMIN");
            admin.setPermissions(Set.of(read, write, delete));
            roleRepository.save(admin);
        };
    }

    @Bean
    public CommandLineRunner initAdminUser(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            if (userRepository.findByEmail("admin@example.com").isEmpty()) {
                Role adminRole = roleRepository.findById("ADMIN")
                        .orElseThrow(() -> new IllegalStateException("ADMIN role not found"));

                User admin = new User();
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("adminpassword"));
                admin.setRoles(Set.of(adminRole));

                userRepository.save(admin);
                System.out.println("Admin user created: admin@example.com / adminpassword");
            }
        };
    }
}
