package com.tjg_project.candy.domain.auth.controller;

import com.tjg_project.candy.domain.auth.entity.RefreshToken;
import com.tjg_project.candy.domain.auth.service.AuthService;
import com.tjg_project.candy.domain.user.entity.Users;
import com.tjg_project.candy.domain.user.service.UsersService;
import com.tjg_project.candy.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthService authService;
    private final UsersService usersService;

    private final Set<String> allowedOrigins = Set.of(
            "http://localhost:3000",
            "https://candy-site.vercel.app"
    );

    @Autowired
    public AuthController(JwtUtil jwtUtil, AuthService authService, UsersService usersService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
        this.usersService = usersService;
    }

    private boolean isLocal(String origin) {
        return origin == null || origin.startsWith("http://localhost");
    }

    /** 공통 쿠키 생성 함수 */
    private ResponseCookie buildCookie(String name, String value, boolean secure, boolean httpOnly) {
        return ResponseCookie.from(name, value)
                .httpOnly(httpOnly)
                .secure(secure)
                .path("/")
                .sameSite("None")
                .maxAge(7 * 24 * 60 * 60)
                .build(); // ❗ domain 절대 넣지 말 것
    }

    /** 로그인 */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user, HttpServletRequest request) {

        Users us = usersService.login(user.getUserId(), user.getPassword());
        if (us == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        Long userId = us.getId();
        String accessToken = jwtUtil.generateAccessToken(userId);
        RefreshToken refresh = authService.createRefreshToken(userId);

        String origin = request.getHeader("Origin");
        boolean secure = !isLocal(origin);

        String csrfToken = UUID.randomUUID().toString();

        ResponseCookie refreshCookie =
                buildCookie("refresh_token", refresh.getToken(), secure, true);

        ResponseCookie csrfCookie =
                buildCookie("XSRF-TOKEN", csrfToken, secure, false);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .header(HttpHeaders.SET_COOKIE, csrfCookie.toString())
                .body(Map.of(
                        "accessToken", accessToken,
                        "role", us.getRole()
                ));
    }

    /** refresh */
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @CookieValue(value = "refresh_token", required = false) String token,
            @CookieValue(value = "XSRF-TOKEN", required = false) String csrfCookie,
            @RequestHeader(value = "X-XSRF-TOKEN", required = false) String csrfHeader,
            HttpServletRequest request
    ) {

        String origin = request.getHeader("Origin");
        String referer = request.getHeader("Referer");

        if (origin == null || !allowedOrigins.contains(origin)) {
            return ResponseEntity.status(403).body(Map.of("error", "Invalid origin"));
        }

        if (referer != null &&
                allowedOrigins.stream().noneMatch(referer::startsWith)) {
            return ResponseEntity.status(403).body(Map.of("error", "Invalid referer"));
        }

        if (token == null) {
            return ResponseEntity.status(401).body(Map.of("error", "No refresh token"));
        }

        if (csrfCookie == null || csrfHeader == null || !csrfCookie.equals(csrfHeader)) {
            return ResponseEntity.status(403).body(Map.of("error", "Invalid CSRF token"));
        }

        Optional<RefreshToken> opt = authService.verifyToken(token);
        if (opt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired refresh token"));
        }

        Long userId = opt.get().getUserId();
        String newAccess = jwtUtil.generateAccessToken(userId);

        boolean secure = !isLocal(origin);

        ResponseCookie refreshCookie =
                buildCookie("refresh_token", opt.get().getToken(), secure, true);

        ResponseCookie csrfCookieNew =
                buildCookie("XSRF-TOKEN", UUID.randomUUID().toString(), secure, false);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .header(HttpHeaders.SET_COOKIE, csrfCookieNew.toString())
                .body(Map.of("accessToken", newAccess));
    }

    /** logout */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue(value = "refresh_token", required = false) String token,
            HttpServletRequest request
    ) {

        if (token != null) {
            authService.verifyToken(token).ifPresent(t -> authService.deleteByUserId(t.getUserId()));
        }

        String origin = request.getHeader("Origin");
        boolean secure = !isLocal(origin);

        ResponseCookie expired = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .sameSite("None")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, expired.toString())
                .body(Map.of("message", "Logged out"));
    }
}


