package com.tjg_project.candy.domain.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

// CsrfController.java (추가)
@RestController
public class CsrfController {
    @GetMapping("/csrf")
    public ResponseEntity<Void> getCsrfToken() {

        String csrfToken = UUID.randomUUID().toString();
        ResponseCookie csrfCookie = ResponseCookie.from("XSRF-TOKEN", csrfToken)
                .httpOnly(false)  // JS가 읽을 수 있어야 함
                .secure(false)    // 배포 시 true
                .path("/")
                .sameSite("Lax")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, csrfCookie.toString())
                .build();
    }
}