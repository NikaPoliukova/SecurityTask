package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


public record RegisterRequest(
        String email,
        String password

) {
}
