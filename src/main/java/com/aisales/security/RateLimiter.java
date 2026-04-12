package com.aisales.security;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiter {

    private final Map<String, Integer> requests = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS = 50;

    public boolean allowed(String userId) {
        int count = requests.getOrDefault(userId, 0);
        if (count >= MAX_REQUESTS) {
            return false;
        }
        requests.put(userId, count + 1);
        return true;
    }

    public void reset(String userId) {
        requests.remove(userId);
    }

    public int getCurrentCount(String userId) {
        return requests.getOrDefault(userId, 0);
    }
}
