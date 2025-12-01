package com.tjg_project.candy.domain.order.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tjg_project.candy.domain.product.entity.Product;
import com.tjg_project.candy.domain.user.entity.Users;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Long cid; // PK

    // ✅ 사용자 (users.id = upk)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upk", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Users user;

    // ✅ 상품 (product.id = ppk)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ppk", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Product product;

    @Column(nullable = false)
    private int qty; // 수량

    @Column(name = "added_at", nullable = false)
    private LocalDateTime addedAt = LocalDateTime.now();
}
