package org.example.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class LoginAttemptService {
    private static final int MAX_ATTEMPTS = 3;

    private final Cache<String, Integer> attemptsCache;

    public LoginAttemptService() {
        this.attemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();
    }

    public void loginSucceeded(String email) {
        attemptsCache.invalidate(email);
    }

    public void loginFailed(String email) {
        int attempts = 0;
        Integer currentAttempts = attemptsCache.getIfPresent(email);
        if (currentAttempts != null) {
            attempts = currentAttempts;
        }
        attempts++;
        attemptsCache.put(email, attempts);
    }

    public boolean isBlocked(String email) {
        Integer attempts = attemptsCache.getIfPresent(email);
        return attempts != null && attempts >= MAX_ATTEMPTS;
    }

    public List<String> getBlockedUsers() {
        return attemptsCache.asMap().entrySet().stream()
                .filter(entry -> entry.getValue() >= MAX_ATTEMPTS)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
