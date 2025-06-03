package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private Set<String> roles;
}

