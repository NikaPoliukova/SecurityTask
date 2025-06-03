package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public record LoginRequest(
        String email,
        String password
) {
}
