package com.tjg_project.candy.domain.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConversionRateDto {

    private Long ppk;            // 상품 PK
    private String productName;  // 상품명
    private long clicks;         // 클릭 수
    private long orders;         // 구매 수
    private double conversionRate;

    public ConversionRateDto(Long ppk, String productName, long clicks, long orders) {
        this.ppk = ppk;
        this.productName = productName;
        this.clicks = clicks;
        this.orders = orders;
        this.conversionRate = clicks == 0 ? 0 : (orders * 100.0 / clicks);
    }
}
