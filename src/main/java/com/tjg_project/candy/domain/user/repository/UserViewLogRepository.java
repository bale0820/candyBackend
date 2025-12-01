package com.tjg_project.candy.domain.user.repository;

import com.tjg_project.candy.domain.user.entity.UserViewLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserViewLogRepository extends JpaRepository<UserViewLog, Long> {

    // 🔥 유저가 최근에 본 로그를 최신순으로 정렬해서 가져오기
    List<UserViewLog> findTop20ByUpkOrderByViewedAtDesc(Long upk);

    // 🔥 최근 본 로그 중 가장 최근 subCategoryId 하나만 가져올 때
    @Query("SELECT l.subCategoryId FROM UserViewLog l WHERE l.upk = :upk ORDER BY l.viewedAt DESC")
    List<Long> findRecentSubCategories(@Param("upk") Long upk);

    Optional<UserViewLog> findByUpkAndPpk(Long upk, Long ppk);

    @Query("SELECT COALESCE(SUM(l.qty), 0) FROM UserViewLog l WHERE l.ppk = :ppk")
    Long sumViewsByProduct(@Param("ppk") Long ppk);
}