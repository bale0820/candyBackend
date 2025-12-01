package com.tjg_project.candy.domain.coupon.repository;
import com.tjg_project.candy.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}

