package com.tjg_project.candy.domain.auth.repository;

import com.tjg_project.candy.domain.auth.entity.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // ✅ 특정 토큰 문자열로 검색
    Optional<RefreshToken> findByToken(String token);

    // ✅ 특정 유저의 모든 리프레시 토큰 삭제 (로그아웃 시)
    @Transactional
    void deleteByUserId(Long userId);
}
