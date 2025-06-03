package org.example.controller;

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

    @GetMapping("/info")
    public String getInfo() {
        return stats.get(random.nextInt(stats.size()));
    }

    @GetMapping("/about")
    public String getAbout() {
        return "This is a public endpoint about the application for non-authenticated users.";
    }
}
