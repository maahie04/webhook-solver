package com.maahie.webhook_solver.service;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    public HttpHeaders createAuthHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token); // Add "Bearer " prefix
        headers.set("Content-Type", "application/json");
        return headers;
    }
}
