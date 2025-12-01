package com.tjg_project.candy.domain.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Users.id
    @Column(nullable = false)
    private Long userId;

    // ✅ 토큰 문자열
    @Column(length = 64, nullable = false, unique = true)
    private String token;

    // ✅ 만료 시간
    @Column(nullable = false)
    private Instant expiryDate;
}
