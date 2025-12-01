package com.tjg_project.candy.domain.analytics.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReviewAnalysisDto {
    private Long ppk;
    private String productName;

    private List<String> tasteKeywords;   // 맛 키워드
    private List<String> positivePoints;  // 고객이 좋아한 포인트
    private List<String> qualityIssues;   // 품질 관련 문제
    private int positiveCount;            // 긍정 개수
    private int negativeCount;            // 부정 개수
}

