//package com.tjg_project.candy.domain.auth.controller;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseCookie;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import jakarta.servlet.http.HttpServletRequest;
//import java.util.UUID;
//
//@RestController
//public class CsrfController {
//
//    private boolean isLocal(String origin) {
//        return origin == null || origin.startsWith("http://localhost");
//    }
//
//    @GetMapping("/csrf")
//    public ResponseEntity<Void> getCsrfToken(HttpServletRequest request) {
//
//        String csrfToken = UUID.randomUUID().toString();
//
//        String origin = request.getHeader("Origin");
//        boolean secure = !isLocal(origin);
//
//        ResponseCookie cookie = ResponseCookie.from("XSRF-TOKEN", csrfToken)
//                .httpOnly(false)
//                .secure(secure)
//                .sameSite("None")
//                .path("/")
//                .maxAge(7 * 24 * 60 * 60)
//                .build();
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, cookie.toString())
//                .build();
//    }
//}

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

    private boolean isLocal(String origin) {
        return origin == null || origin.startsWith("http://localhost");
    }

    @GetMapping("/csrf")
    public ResponseEntity<Void> getCsrfToken(HttpServletRequest request) {

        String csrfToken = UUID.randomUUID().toString();

        String origin = request.getHeader("Origin");
        boolean secure = !isLocal(origin);

        ResponseCookie cookie = ResponseCookie.from("XSRF-TOKEN", csrfToken)
                .httpOnly(false)
                .secure(secure)
                .sameSite("None")
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header("X-CSRF-TOKEN", csrfToken)    // ★ 헤더도 같이 보내기
                .build();
    }
}
