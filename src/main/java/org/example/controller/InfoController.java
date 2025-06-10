package org.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/v1")
public class InfoController {
    private final List<String> stats = List.of(
            "MVC application",
            "Spring Boot 2.7 app",
            "Active users: 10",
            "Version: 1.0.1",
            "Server time: " + System.currentTimeMillis()
    );

    private final Random random = new Random();

    @PreAuthorize("hasAuthority('VIEW_INFO')")
    @GetMapping("/info")
    public ResponseEntity<String> getInfo() {
        return ResponseEntity.ok(stats.get(random.nextInt(stats.size())));
    }

    @GetMapping("/about")
    public String getAbout() {
        return "This is a public endpoint about the application for non-authenticated users.";
    }
}
