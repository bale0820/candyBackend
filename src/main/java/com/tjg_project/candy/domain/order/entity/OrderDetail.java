package com.tjg_project.candy.domain.order.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tjg_project.candy.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "order_detail")
public class OrderDetail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

    private Long ppk;
    private String productName;
    private int qty;
    private int price;

    // ⭐ 정석 JPA 매핑 추가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ppk", referencedColumnName = "id", insertable = false, updatable = false)
    private Product product;
}
