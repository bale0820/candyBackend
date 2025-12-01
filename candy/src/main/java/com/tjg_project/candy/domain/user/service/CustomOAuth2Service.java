package com.tjg_project.candy.domain.user.service;

import com.tjg_project.candy.domain.user.entity.Users;
import com.tjg_project.candy.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomOAuth2Service {
    private final UserRepository userRepository;

    public CustomOAuth2Service(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long saveOrUpdate(Map<String, Object> attributes, String provider) {
        final String email;
        final String name;

        if ("naver".equals(provider)) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            email = (String) response.get("email");
            name = (String) response.get("name");
        } else if ("kakao".equals(provider)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            String kakaoEmail = (String) kakaoAccount.get("email");
            String kakaoNickname = (String) profile.get("nickname");
            Long kakaoId = (Long) attributes.get("id");
            email = kakaoEmail != null ? kakaoEmail : "kakao_" + kakaoId + "@kakao.local";
            name = kakaoNickname;
        } else {
            throw new IllegalArgumentException("지원되지 않는 provider입니다: " + provider);
        }

        if (email == null)
            throw new IllegalArgumentException("소셜 로그인 이메일 정보를 찾을 수 없습니다.");

        Optional<Users> existing = userRepository.findByNameAndProvider(name, provider);

        // ✅ 기존 회원이 있으면 업데이트 후 ID 반환
        if (existing.isPresent()) {
            Users user = existing.get();
            user.setName(name);
            user.setProvider(provider);
            userRepository.save(user);
            return user.getId();
        }

        // ✅ 없으면 신규 생성 후 ID 반환
        Users newUser = new Users();
        newUser.setUserId(UUID.randomUUID().toString());
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setProvider(provider);
        newUser.setPassword("SOCIAL_LOGIN_USER");

        Users savedUser = userRepository.save(newUser);
        return savedUser.getId();
    }
}
