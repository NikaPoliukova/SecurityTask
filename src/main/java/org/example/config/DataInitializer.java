package org.example.config;

import org.example.entity.Permission;
import org.example.entity.Role;
import org.example.repository.PermissionRepository;
import org.example.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
