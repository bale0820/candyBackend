package com.tjg_project.candy.domain.order.controller;

import com.tjg_project.candy.domain.coupon.service.CouponService;
import com.tjg_project.candy.domain.order.dto.NaverApproveResponse;
import com.tjg_project.candy.domain.order.entity.KakaoPay;
import com.tjg_project.candy.domain.order.entity.NaverPay;
import com.tjg_project.candy.domain.order.service.OrderService;
import com.tjg_project.candy.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/payment/naver")
@RequiredArgsConstructor
public class NaverPayController {
    private final OrderService orderService;
    private final CouponService couponService;
    private final ProductService productService;
    private NaverPay payInfo = null;

    /** ✅ 프론트에서 주문 생성 요청 시 merchantPayKey 발급 */
    @PostMapping("/order")
    public Map<String, String> createOrder(@RequestBody NaverPay naverPay) {
        System.out.println("naver" + naverPay);
        String merchantPayKey = UUID.randomUUID().toString();
        System.out.println("🧾 네이버페이 주문 생성 완료 - merchantPayKey: " + merchantPayKey);
        System.out.println("📦 받은 주문 정보: " + naverPay);
        payInfo = naverPay;
        Map<String, String> res = new HashMap<>();
        res.put("merchantPayKey", merchantPayKey);
        return res;
    }

    /** ✅ 결제 완료 후 콜백 URL */
    @GetMapping("/return")
    public ResponseEntity<Void> naverPayReturn(@RequestParam Map<String, String> params) {
        String tid = "N" + UUID.randomUUID().toString().replace("-", "").substring(0, 18);
        String orderId = UUID.randomUUID().toString();
        NaverApproveResponse approve = new NaverApproveResponse();
        approve.setTid(tid);
        payInfo.setOrderId(orderId);
        orderService.saveOrder(approve, payInfo);
//        return "<h2>✅ 네이버페이 테스트 결제 완료!</h2><p>주문번호: " + params.get("merchantPayKey") + "</p>";

        couponService.updateCoupon(payInfo.getCouponId());

        List<KakaoPay.ProductInfo> productInfo = payInfo.getProductInfo();

        productService.updateCount(productInfo);

        URI redirect = URI.create("https://candy-site.vercel.app/payResult?orderId=" + orderId + "&status=success");
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(redirect);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
