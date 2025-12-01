package com.tjg_project.candy.domain.coupon.service;

import com.tjg_project.candy.domain.coupon.entity.UserCoupon;

import java.util.List;

public interface CouponService {

    boolean issueCouponToUser(Long userId, Long couponId);

    boolean deleteUserCoupon(Long userId, Long couponId);

    List<UserCoupon> getUserCoupons(Long userId);

    boolean updateCoupon(Long id);
}
