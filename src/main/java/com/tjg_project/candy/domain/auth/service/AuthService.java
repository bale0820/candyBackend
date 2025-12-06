package com.tjg_project.candy.domain.auth.service;

import com.tjg_project.candy.domain.auth.entity.RefreshToken;
import com.tjg_project.candy.domain.auth.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(Long userId) {
        String rawToken = UUID.randomUUID().toString();
        String hashedToken = DigestUtils.sha256Hex(rawToken);

        RefreshToken entity = new RefreshToken();
        entity.setUserId(userId);
        entity.setToken(hashedToken);
        entity.setExpiryDate(Instant.now().plusSeconds(7 * 24 * 60 * 60));
        refreshTokenRepository.save(entity);

        // ✅ DB용과 응답용 객체를 분리 (dirty checking 차단)
        RefreshToken response = new RefreshToken();
        response.setUserId(userId);
        response.setToken(rawToken);
        response.setExpiryDate(entity.getExpiryDate());

        return response;
    }

    @Transactional
    public Optional<RefreshToken> verifyToken(String token) {
        String hashed = DigestUtils.sha256Hex(token);
        Optional<RefreshToken> existing = refreshTokenRepository.findByToken(hashed);
        if (existing.isEmpty()) return Optional.empty();
        RefreshToken found = existing.get();

        if (found.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.deleteById(found.getId());
            return Optional.empty();
        }

        Long userId = found.getUserId();
        refreshTokenRepository.delete(found);
        RefreshToken newToken = createRefreshToken(userId);

        return Optional.of(newToken);
    }

    /**
     * ✅ 로그아웃 시 삭제
     */
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
