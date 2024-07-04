package com.example.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class WelcomeController {
    @GetMapping("/backendtest")
    public ResponseEntity<String> testing() {
        String htmlContent = "<!DOCTYPE html>" +
                             "<html lang=\"en\">" +
                             "<head>" +
                             "<meta charset=\"UTF-8\">" +
                             "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                             "<title>Backend Test</title>" +
                             "<style>" +
                             "body { background-color: #f0f0f0; font-family: Arial, sans-serif; }" +
                             "h1 { color: #2c3e50; text-align: center; margin-top: 20%; }" +
                             "</style>" +
                             "</head>" +
                             "<body>" +
                             "<h1>EKA backend started</h1>" +
                             "</body>" +
                             "</html>";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/html; charset=UTF-8");

        return ResponseEntity.ok().headers(headers).body(htmlContent);
    }
}
