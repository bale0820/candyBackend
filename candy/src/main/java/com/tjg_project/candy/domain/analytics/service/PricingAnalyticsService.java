package com.tjg_project.candy.domain.analytics.service;

import com.tjg_project.candy.domain.analytics.dto.ProductPricingStatsDto;
import com.tjg_project.candy.domain.order.repository.OrderDetailRepository;
import com.tjg_project.candy.domain.product.entity.Product;
import com.tjg_project.candy.domain.product.repository.ProductRepository;
import com.tjg_project.candy.domain.user.repository.UserViewLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PricingAnalyticsService {

    private final UserViewLogRepository userViewLogRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    // -------------------------------
    // ⭐ 단일 상품 분석
    // -------------------------------
    public ProductPricingStatsDto getStats(Long ppk) {

        Product product = productRepository.findById(ppk)
                .orElseThrow(() -> new RuntimeException("상품 없음"));

        long clicks = userViewLogRepository.sumViewsByProduct(ppk);
        long orders = Optional.ofNullable(orderDetailRepository.countOrdersByProduct(ppk))
                .orElse(0L);

        double conversionRate = (clicks == 0) ? 0 : (orders * 100.0 / clicks);

        int currentPrice = product.getPrice();

        // 1) 가격 민감도
        double priceSensitivity = conversionRate / currentPrice;

        // 2) AI 추천 가격(5% 할인)
        double discountRate = 0.05;
        int aiLowerPrice = (int) (currentPrice * (1 - discountRate));

        // 3) 할인 시 예측 전환율
        double predictedConversionRate =
                conversionRate * (1 + priceSensitivity * discountRate * 1000);

        // 4) 클릭률 증가율
        double aiClickRate =
                predictedConversionRate + (clicks * priceSensitivity * 0.1);

        // 5) 예측 구매수
        double predictedOrders = clicks * (predictedConversionRate / 100.0);

        // 6) 현재 매출
        double currentRevenue = orders * currentPrice;

        // 7) 예측 매출
        double predictedRevenue = predictedOrders * aiLowerPrice;

        // 8) 매출 증가량
        double revenueGain = predictedRevenue - currentRevenue;

        // 9) 매출 증가율
        double revenueGainPercent = currentRevenue == 0 ? 0 :
                (revenueGain / currentRevenue) * 100;

        // 10) 가격 탄력성 PED
        double PED = priceSensitivity * 100;

        // 11) 최적 가격 (매출 최대점)
        double optimalPrice = (PED == -1) ? currentPrice :
                (PED / (PED + 1)) * currentPrice;

        return new ProductPricingStatsDto(
                product.getId(),
                product.getProductName(),
                currentPrice,
                clicks,
                orders,
                conversionRate,
                aiLowerPrice,
                predictedConversionRate,
                aiClickRate,
                priceSensitivity,
                predictedOrders,
                currentRevenue,
                predictedRevenue,
                revenueGain,
                revenueGainPercent,
                PED,
                optimalPrice
        );
    }

    // -------------------------------
    // ⭐ 전체 상품 분석
    // -------------------------------
    public List<ProductPricingStatsDto> getAllStats() {

        return productRepository.findAll().stream().map(product -> {

            long clicks = userViewLogRepository.sumViewsByProduct(product.getId());
            long orders = Optional.ofNullable(orderDetailRepository.countOrdersByProduct(product.getId()))
                    .orElse(0L);

            double conversionRate = (clicks == 0) ? 0 : (orders * 100.0 / clicks);
            int currentPrice = product.getPrice();

            double priceSensitivity = conversionRate / currentPrice;
            double discountRate = 0.05;
            int aiLowerPrice = (int) (currentPrice * (1 - discountRate));

            double predictedConversionRate =
                    conversionRate * (1 + priceSensitivity * discountRate * 1000);

            double aiClickRate =
                    predictedConversionRate + (clicks * priceSensitivity * 0.1);

            double predictedOrders = clicks * (predictedConversionRate / 100.0);
            double currentRevenue = orders * currentPrice;
            double predictedRevenue = predictedOrders * aiLowerPrice;

            double revenueGain = predictedRevenue - currentRevenue;
            double revenueGainPercent = currentRevenue == 0 ? 0 :
                    (revenueGain / currentRevenue) * 100;

            double PED = priceSensitivity * 100;
            double optimalPrice = (PED == -1) ? currentPrice :
                    (PED / (PED + 1)) * currentPrice;

            return new ProductPricingStatsDto(
                    product.getId(),
                    product.getProductName(),
                    currentPrice,
                    clicks,
                    orders,
                    conversionRate,
                    aiLowerPrice,
                    predictedConversionRate,
                    aiClickRate,
                    priceSensitivity,
                    predictedOrders,
                    currentRevenue,
                    predictedRevenue,
                    revenueGain,
                    revenueGainPercent,
                    PED,
                    optimalPrice
            );
        }).toList();
    }
}
