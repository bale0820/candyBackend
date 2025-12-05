package com.tjg_project.candy.domain.order.controller;

import com.tjg_project.candy.domain.coupon.service.CouponService;
import com.tjg_project.candy.domain.order.dto.KakaoApproveResponse;
import com.tjg_project.candy.domain.order.dto.KakaoReadyResponse;
import com.tjg_project.candy.domain.order.entity.KakaoPay;
import com.tjg_project.candy.domain.order.service.KakaoPayService;
import com.tjg_project.candy.domain.order.service.OrderService;
import com.tjg_project.candy.domain.product.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/payment")
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;
    private final OrderService orderService;
    private final CouponService couponService;
    private final ProductService productService;

    /** 🔥 전역 payInfo 제거 → orderId 기반 저장 */
    private final Map<String, KakaoPay> payStore = new ConcurrentHashMap<>();

    @Autowired
    public KakaoPayController(
            KakaoPayService kakaoPayService,
            OrderService orderService,
            CouponService couponService,
            ProductService productService
    ) {
        this.kakaoPayService = kakaoPayService;
        this.orderService = orderService;
        this.couponService = couponService;
        this.productService = productService;
    }

    /** ✅ 결제 준비 */
    @PostMapping("/kakao/ready")
    public KakaoReadyResponse ready(@RequestBody KakaoPay kakaoPay) {

        String orderId = UUID.randomUUID().toString();
        kakaoPay.setOrderId(orderId);

        // 🔥 Controller 전역변수 대신 안전한 Map 저장
        payStore.put(orderId, kakaoPay);

        return kakaoPayService.ready(kakaoPay);
    }

    /** ✅ 성공 콜백 — 트랜잭션으로 묶고 커넥션 누수 완전 제거 */
    @Transactional
    @GetMapping("/qr/success")
    public ResponseEntity<Void> success(@RequestParam String orderId, @RequestParam("pg_token") String pgToken) {

        /** 🔥 중복 호출 방지 — remove 하면 두 번째 호출은 null */
        KakaoPay payInfo = payStore.remove(orderId);
        if (payInfo == null) {
            System.out.println("⚠ 이미 처리된 orderId: " + orderId);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // 1) 카카오 승인 요청
        KakaoApproveResponse approve = kakaoPayService.approve(orderId, pgToken);

        // 2) 주문 저장
        orderService.saveOrder(approve, payInfo);

        // 3) 쿠폰 소모
        couponService.updateCoupon(payInfo.getCouponId());

        // 4) 재고 업데이트
        List<KakaoPay.ProductInfo> productInfo = payInfo.getProductInfo();
        productService.updateCount(productInfo);

        // 5) 리다이렉트
        URI redirect = URI.create("https://candy-site.vercel.app/payResult?orderId="
                + orderId + "&status=success");

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(redirect);

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/qr/cancel")
    public ResponseEntity<?> cancel(@RequestParam String orderId) {
        return ResponseEntity.ok(Map.of("status", "CANCEL", "orderId", orderId));
    }

    @GetMapping("/qr/fail")
    public ResponseEntity<?> fail(@RequestParam String orderId) {
        return ResponseEntity.ok(Map.of("status", "FAIL", "orderId", orderId));
    }
}
