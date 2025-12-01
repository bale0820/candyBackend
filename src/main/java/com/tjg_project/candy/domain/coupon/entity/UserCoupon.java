package com.tjg_project.candy.domain.coupon.entity;

import com.tjg_project.candy.domain.user.entity.Users;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(
        name = "user_coupon",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id","coupon_id"})}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private Users users; // FK로 Users.id 사용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
//    @JsonIgnore
    private Coupon coupon;

    @Column(name = "qty")
    private int qty;

    @Column(nullable = false)
    private Boolean isUsed = false;
}
