package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.RegisterRequest;
import org.example.entity.User;
import org.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;


    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "blocked", required = false) String blocked,
                            Model model) {
        if (blocked != null) {
            model.addAttribute("errorMessage", "Your account is temporarily locked. Try again in 5 minutes.");
        } else if (error != null) {
            model.addAttribute("errorMessage", "Invalid email or password");
        }
        return "login";
    }


    @GetMapping("/logout-success")
    public String logoutPage() {
        return "logout-success";
    }

    @GetMapping("/success-login")
    public String loginSuccessPage(Principal principal, Model model) {
        String email = principal.getName();
        model.addAttribute("message", "User " + email + " successfully login");
        return "success-login";
    }
    @GetMapping("/success-register")
    public String registerSuccessPage(Principal principal, Model model) {
        String email = principal.getName();
        model.addAttribute("message",  email + " successfully register");
        return "success-register";
    }


    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute RegisterRequest request, Model model) {
        try {
            User user = userService.register(request);
            model.addAttribute("message", "User " + user.getEmail() + " successfully registered");
            return "success-register";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }

}
