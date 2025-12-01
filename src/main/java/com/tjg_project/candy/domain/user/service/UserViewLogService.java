package com.tjg_project.candy.domain.user.service;

import com.tjg_project.candy.domain.user.entity.UserViewLog;
import com.tjg_project.candy.domain.user.repository.UserViewLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserViewLogService {

    private final UserViewLogRepository userViewLogRepository;

    // 최근 본 제품 저장
    public void saveViewLog(Long upk, Long ppk, Long subCategoryId) {

        if (upk == null) return;

        // 기존 로그 있는지 먼저 확인
        Optional<UserViewLog> optionalLog = userViewLogRepository.findByUpkAndPpk(upk, ppk);

        if (optionalLog.isPresent()) {
            // 같은 유저가 같은 상품 다시 조회함 → qty + 1
            UserViewLog log = optionalLog.get();
            log.setQty(log.getQty() + 1);
            log.setViewedAt(LocalDateTime.now());  // 최근 조회시간도 갱신
            userViewLogRepository.save(log);
            return;
        }

        // 처음 조회한 상품이면 새로 저장
        UserViewLog newLog = new UserViewLog();
        newLog.setUpk(upk);
        newLog.setPpk(ppk);
        newLog.setSubCategoryId(subCategoryId);

        userViewLogRepository.save(newLog);
    }

    // 가장 최근 subCategoryId 반환
    public Long getRecentSubCategory(Long upk) {
        List<Long> list = userViewLogRepository.findRecentSubCategories(upk);

        System.out.println("list : " + list);

        if (list.isEmpty()) return null;
        return list.get(0); // 최신 항목 1개
    }
}
