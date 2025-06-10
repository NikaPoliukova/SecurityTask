package org.example.handler;

import lombok.RequiredArgsConstructor;
import org.example.service.LoginAttemptService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final LoginAttemptService loginAttemptService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String email = request.getParameter("email");

        loginAttemptService.loginFailed(email);

        if (loginAttemptService.isBlocked(email)) {
            response.sendRedirect("/login?blocked=true");
        } else {
            response.sendRedirect("/login?error=true");
        }
    }
}

