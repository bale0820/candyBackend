package com.tjg_project.candy.domain.coupon.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "coupon")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {

    @Id
    @Column(name = "coupon_id")
    private Long couponId;

    @Column(name = "coupon_dc_rate")
    private int couponDcRate;

    @Column(name = "coupon_qty")
    private int couponQty;
}

