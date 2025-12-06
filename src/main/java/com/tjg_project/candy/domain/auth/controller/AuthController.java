package com.tjg_project.candy.domain.auth.controller;

import com.tjg_project.candy.domain.auth.entity.RefreshToken;
import com.tjg_project.candy.domain.auth.service.AuthService;
import com.tjg_project.candy.domain.user.entity.Users;
import com.tjg_project.candy.domain.user.service.UsersService;
import com.tjg_project.candy.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    /**
     * 환경에 따라 secure 자동 적용
     */
    private boolean isLocalhost(String origin) {
        return origin != null && origin.startsWith("http://localhost");
    }

    private ResponseCookie buildCookie(String name, String value, boolean secure) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(secure)              // 🔥 HTTPS 환경에서는 반드시 true
                .path("/")
                .sameSite("None")           // 🔥 cross-site 전송 허용
                .maxAge(7 * 24 * 60 * 60)
                .build();
    }

    private ResponseCookie buildCsrfCookie(String value, boolean secure) {
        return ResponseCookie.from("XSRF-TOKEN", value)
                .httpOnly(false)
                .secure(secure)
                .path("/")
                .sameSite("None")
                .maxAge(7 * 24 * 60 * 60)
                .build();
    }

    /**
     * ✅ 로그인 처리
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user, HttpServletRequest request) {

        Users us = usersService.login(user.getUserId(), user.getPassword());
        if (us == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        Long userId = us.getId();
        String accessToken = jwtUtil.generateAccessToken(userId);
        RefreshToken refresh = authService.createRefreshToken(userId);

        String csrfToken = UUID.randomUUID().toString();

        boolean secure = !isLocalhost(request.getHeader("Origin"));

        // refresh token
        ResponseCookie refreshCookie = buildCookie("refresh_token", refresh.getToken(), secure);

        // csrf token
        ResponseCookie csrfCookie = buildCsrfCookie(csrfToken, secure);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .header(HttpHeaders.SET_COOKIE, csrfCookie.toString())
                .body(Map.of(
                        "accessToken", accessToken,
                        "role", us.getRole()
                ));
    }

    /**
     * 🔄 토큰 재발급(refresh)
     */
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

        if (referer != null && allowedOrigins.stream().noneMatch(referer::startsWith)) {
            return ResponseEntity.status(403).body("Invalid Referer");
        }

        if (token == null)
            return ResponseEntity.status(401).body(Map.of("error", "No refresh token"));

        // CSRF 검사
        if (csrfCookie == null || csrfHeader == null || !csrfCookie.equals(csrfHeader)) {
            return ResponseEntity.status(403).body(Map.of("error", "Invalid CSRF token"));
        }

        Optional<RefreshToken> newRefreshOpt = authService.verifyToken(token);
        if (newRefreshOpt.isEmpty())
            return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired refresh token"));

        RefreshToken newRefresh = newRefreshOpt.get();
        Long userId = newRefresh.getUserId();
        String newAccessToken = jwtUtil.generateAccessToken(userId);

        boolean secure = !isLocalhost(origin);

        ResponseCookie refreshCookie = buildCookie("refresh_token", newRefresh.getToken(), secure);
        ResponseCookie csrfCookieNew = buildCsrfCookie(UUID.randomUUID().toString(), secure);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .header(HttpHeaders.SET_COOKIE, csrfCookieNew.toString())
                .body(Map.of("accessToken", newAccessToken));
    }

    /**
     * 🚪 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue(value = "refresh_token", required = false) String token,
            HttpServletRequest request
    ) {

        if (token != null) {
            authService.verifyToken(token).ifPresent(t -> authService.deleteByUserId(t.getUserId()));
        }

        boolean secure = !isLocalhost(request.getHeader("Origin"));

        ResponseCookie expiredCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .sameSite("None")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, expiredCookie.toString())
                .body(Map.of("message", "Logged out"));
    }
}
