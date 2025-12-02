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
            "https://candy-site-e28z.vercel.app"
    );

    @Autowired
    public AuthController(JwtUtil jwtUtil, AuthService authService, UsersService usersService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
        this.usersService = usersService;
    }

    /**
     * ✅ 로그인 처리
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user) {
        Users us = usersService.login(user.getUserId(), user.getPassword());

        if (us == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        Long userId = us.getId(); // ✅ 실제 유저 PK
        String accessToken = jwtUtil.generateAccessToken(userId);
        RefreshToken refresh = authService.createRefreshToken(userId);

        // ✅ HttpOnly 쿠키에 RefreshToken 저장
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refresh.getToken())
                .httpOnly(true)
                .secure(false) // 배포 시 true
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Lax")
                .build();

        String csrfToken = UUID.randomUUID().toString();
        ResponseCookie csrfCookie = ResponseCookie.from("XSRF-TOKEN", csrfToken)
                .httpOnly(false)  // JS가 읽을 수 있어야 함
                .secure(false)    // 배포 시 true
                .path("/")
                .sameSite("Lax")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.SET_COOKIE, csrfCookie.toString())
                .body(Map.of("accessToken", accessToken,
                        "role", us.getRole()
                ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @CookieValue(value = "refresh_token", required = false) String token,
            @CookieValue(value = "XSRF-TOKEN", required = false) String csrfCookie,
            @RequestHeader(value = "X-XSRF-TOKEN", required = false) String csrfHeader,
            HttpServletRequest request
    ) {

        // ✅ Origin 검증 (CORS 허용 도메인만 통과)
        String origin = request.getHeader("Origin");
        String referer = request.getHeader("Referer");
        System.out.println("origin"+ origin);
        if (origin == null || !allowedOrigins.contains(origin)) {
            return ResponseEntity.status(403).body(Map.of("error", "Invalid origin"));
        }

        if (referer != null) {
            if (allowedOrigins.stream().noneMatch(referer::startsWith)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Invalid Referer");
            }
        }

        if (token == null)
            return ResponseEntity.status(401).body(Map.of("error", "No refresh token"));

        // ✅ CSRF Double Submit 검사
        if (csrfCookie == null || csrfHeader == null || !csrfCookie.equals(csrfHeader)) {
            return ResponseEntity.status(403).body(Map.of("error", "Invalid CSRF token"));
        }

        Optional<RefreshToken> newRefreshOpt = authService.verifyToken(token);
        if (newRefreshOpt.isEmpty())
            return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired refresh token"));

        RefreshToken newRefresh = newRefreshOpt.get();
        Long userId = newRefresh.getUserId();
        String newAccessToken = jwtUtil.generateAccessToken(userId);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", newRefresh.getToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        // ✅ 새로운 CSRF 토큰 생성 (이게 중요)
        String newCsrf = UUID.randomUUID().toString();
        ResponseCookie csrfCookieNew = ResponseCookie.from("XSRF-TOKEN", newCsrf)
                .httpOnly(false)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.SET_COOKIE, csrfCookieNew.toString())
                .body(Map.of("accessToken", newAccessToken
                        ));
    }

    /**
     * ✅ 로그아웃 처리
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(value = "refresh_token", required = false) String token) {
        System.out.println("✅ verifyToken() token = " + token);
        if (token != null) {
            authService.verifyToken(token).ifPresent(t -> authService.deleteByUserId(t.getUserId()));
        }
        ResponseCookie expiredCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, expiredCookie.toString())
                .body(Map.of("message", "Logged out"));
    }
}
