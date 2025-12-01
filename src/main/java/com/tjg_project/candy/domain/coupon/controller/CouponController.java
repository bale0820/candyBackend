package com.tjg_project.candy.domain.coupon.controller;

import com.tjg_project.candy.domain.coupon.entity.UserCoupon;
import com.tjg_project.candy.domain.coupon.repository.UserCouponRepository;
import com.tjg_project.candy.domain.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private final UserCouponRepository userCouponRepository;

    /** 🔹 1) 쿠폰 발급 */
    @PostMapping("/issue/{couponId}")
    public ResponseEntity<?> issueCoupon(
            @PathVariable Long couponId,
            @RequestBody Map<String, Long> body
    ) {
        if (body == null || !body.containsKey("userId")) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", "fail", "message", "userId가 필요합니다.")
            );
        }

        Long userId = body.get("userId");
        boolean ok = couponService.issueCouponToUser(userId, couponId);

        if (ok) {
            return ResponseEntity.ok(Map.of("status", "success", "message", "쿠폰 발급 완료"));
        } else {
            return ResponseEntity.status(400).body(
                    Map.of("status", "fail", "message", "이미 받은 쿠폰입니다.")
            );
        }
    }

    /** 🔹 2) 마이페이지에서 쓸 전체 쿠폰 정보 (UserCoupon 전체) */
    @GetMapping("/my/{userId}")
    public ResponseEntity<?> getUserCoupons(@PathVariable Long userId) {
        System.out.println("mypage userID-------?" + userId);
        List<UserCoupon> coupons = couponService.getUserCoupons(userId);
        System.out.println("coupons ****************************" + coupons);
        return ResponseEntity.ok(coupons);
    }


    /** 🔹 3) 쿠폰 페이지에서 쓸 "이미 받은 쿠폰 ID 목록" ([1,2,3]) */
    @GetMapping("/user-ids/{userId}")
    public ResponseEntity<?> getUserCouponIds(@PathVariable Long userId) {

        List<UserCoupon> list = couponService.getUserCoupons(userId);

        List<Long> couponIds = list.stream()
                .map(uc -> uc.getCoupon().getCouponId())
                .toList();

        return ResponseEntity.ok(couponIds);
    }

    /** 🔹 4) 쿠폰 삭제  */
    @DeleteMapping("/deleteCoupon/{userId}/{couponId}")
    public ResponseEntity<?> deleteCoupon(
            @PathVariable Long userId,
            @PathVariable Long couponId
    ) {
        boolean deleted = couponService.deleteUserCoupon(userId, couponId);

        if (deleted) return ResponseEntity.ok("deleted");
        else return ResponseEntity.status(400).body("not found");
    }

}
