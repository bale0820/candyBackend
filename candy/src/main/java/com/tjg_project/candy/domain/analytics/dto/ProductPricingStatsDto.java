package com.tjg_project.candy.domain.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductPricingStatsDto {

    private Long ppk;               // 상품 PK
    private String productName;     // 상품명
    private int currentPrice;       // 현재 가격

    // 실측 성능
    private long clicks;            // 클릭수
    private long orders;            // 구매수
    private double conversionRate;  // 실제 전환율 (%)

    // AI 예측
    private int aiLowerPrice;       // AI가 추천하는 가격
    private double aiConversionRate;// 할인 시 예측 전환율 (%)
    private double aiClickRate;     // 할인 시 예측 클릭률 변화 (%)

    // 고급 지표
    private double priceSensitivity; // 가격 민감도
    private double predictedOrders;  // 할인 시 예상 구매수
    private double currentRevenue;   // 현재 매출
    private double predictedRevenue; // 할인 시 예상 매출
    private double revenueGain;      // 매출 증가 분
    private double revenueGainPercent; // 매출 증가율 (%)
    private double ped;             // 가격탄력성 Price Elasticity of Demand
    private double optimalPrice;    // 매출 최대점 최적 가격
}
