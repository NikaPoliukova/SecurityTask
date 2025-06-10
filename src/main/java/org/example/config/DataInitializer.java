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

            Permission viewInfo = new Permission();
            viewInfo.setName("VIEW_INFO");

            Permission viewAdmin = new Permission();
            viewAdmin.setName("VIEW_ADMIN");

            permissionRepository.saveAll(List.of(read, write, delete, viewInfo, viewAdmin));

            Role user = new Role();
            user.setName("USER");
            user.setPermissions(Set.of(read));
            roleRepository.save(user);

            Role infoViewer = new Role();
            infoViewer.setName("INFO_VIEWER");
            infoViewer.setPermissions(Set.of(viewInfo));
            roleRepository.save(infoViewer);

            Role adminViewer = new Role();
            adminViewer.setName("ADMIN_VIEWER");
            adminViewer.setPermissions(Set.of(viewAdmin));
            roleRepository.save(adminViewer);

            Role fullAccess = new Role();
            fullAccess.setName("ADMIN");
            fullAccess.setPermissions(Set.of(viewInfo, viewAdmin, read, write, delete));
            roleRepository.save(fullAccess);
        };
    }

    @Bean
    public CommandLineRunner initAdminUser(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            if (userRepository.findByEmail("admin@example.com").isEmpty()) {
                Role fullAccess = roleRepository.findById("ADMIN")
                        .orElseThrow(() -> new IllegalStateException("ADMIN role not found"));

                User admin = new User();
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("adminpassword"));
                admin.setRoles(Set.of(fullAccess));

                userRepository.save(admin);
                System.out.println("Admin user created: admin@example.com / adminpassword");
            }
            if (userRepository.findByEmail("info@example.com").isEmpty()) {
                Role infoViewer = roleRepository.findById("INFO_VIEWER")
                        .orElseThrow(() -> new IllegalStateException("INFO_VIEWER role not found"));

                User infoUser = new User();
                infoUser.setEmail("info@example.com");
                infoUser.setPassword(passwordEncoder.encode("password"));
                infoUser.setRoles(Set.of(infoViewer));
                userRepository.save(infoUser);
            }

            if (userRepository.findByEmail("viewer@example.com").isEmpty()) {
                Role adminViewer = roleRepository.findById("ADMIN_VIEWER")
                        .orElseThrow(() -> new IllegalStateException("ADMIN_VIEWER role not found"));
                Role infoViewer = roleRepository.findById("INFO_VIEWER")
                        .orElseThrow(() -> new IllegalStateException("INFO_VIEWER role not found"));

                User viewerUser = new User();
                viewerUser.setEmail("viewer@example.com");
                viewerUser.setPassword(passwordEncoder.encode("password"));
                viewerUser.setRoles(Set.of(infoViewer, adminViewer));
                userRepository.save(viewerUser);
            }

        };
    }
}
