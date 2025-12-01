package com.tjg_project.candy.global.config;

import com.tjg_project.candy.domain.user.controller.CustomOAuth2SuccessHandler;
import com.tjg_project.candy.global.util.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    public SecurityConfig(JwtFilter jwtFilter, CustomOAuth2SuccessHandler customOAuth2SuccessHandler) {
        this.jwtFilter = jwtFilter;
        this.customOAuth2SuccessHandler = customOAuth2SuccessHandler; // ✅ 주입
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .formLogin(form -> form.disable())   // ✅ 기본 폼 로그인 끄기
                .httpBasic(basic -> basic.disable())
                .exceptionHandling(ex -> ex
                        // ✅ 302 대신 401로 응답
                        .authenticationEntryPoint((req, res, e) -> res.setStatus(HttpServletResponse.SC_UNAUTHORIZED))
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/", "/login", "/product/**", "/notice/**", "/member/**",
                                "/orders/**", "/payment/**", "/delivery/**", "/auth/**", "/oauth2/**", "/csrf",
                                "/view/**", "/category/**","/coupon/**","/recipe/**","/images/**","/api/forecast/**","/api/forecast/predict/**","/api/chatbot/**","/api/analytics/conversion/**","/excel/**","/api/admin/pricing/**","/api/admin/reviews/**","/advertise/**").permitAll().anyRequest().authenticated())
                .oauth2Login(oauth -> oauth     // ✅ OAuth2 로그인 활성화
                        .successHandler(customOAuth2SuccessHandler)// 로그인 성공 후 리다이렉트 URL
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // ✅ CORS 허용 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "https://localhost:3000", "http://127.0.0.1:3000")); // 프론트 도메인
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // 쿠키/헤더 인증 허용
        configuration.setExposedHeaders(List.of("Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}