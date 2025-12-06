package com.tjg_project.candy.domain.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
public class CsrfController {

    private boolean isLocalhost(String origin) {
        return origin != null && origin.startsWith("http://localhost");
    }

    @GetMapping("/csrf")
    public ResponseEntity<Void> getCsrfToken(HttpServletRequest request) {

        String csrfToken = UUID.randomUUID().toString();

        String origin = request.getHeader("Origin");
        boolean secure = !isLocalhost(origin);

        ResponseCookie csrfCookie = ResponseCookie.from("XSRF-TOKEN", csrfToken)
                .httpOnly(false)
                .secure(secure)
                .path("/")
                .domain("candybackend-6skt.onrender.com")   // ★ 핵심
                .sameSite("None")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, csrfCookie.toString())
                .build();
    }
}
