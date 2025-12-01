package com.tjg_project.candy.domain.order.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_code", nullable = false, unique = true, length = 50)
    private String orderCode; // UUID
    private Long upk;

    private int totalAmount;
    private int shippingFee;
    private int discountAmount;

    private String receiverName;
    private String receiverPhone;
    private String zipcode;
    private String address1;
    private String address2;
    private String memo;

    private LocalDateTime odate = LocalDateTime.now();
    private LocalDateTime shippingAt;   // 배송 출발 시간
    private LocalDateTime deliveredAt;  // 배송 완료 시간
    private LocalDateTime eta;          // 도착 예정 시간

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", length = 20)
    private DeliveryStatus deliveryStatus;   // null 허용

    @Column(length = 50)
    private String tid; // 카카오 TID

    // ✅ 주문 상세 리스트
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonManagedReference
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public void addOrderDetail(OrderDetail detail) {
        orderDetails.add(detail);
        detail.setOrder(this);
    }
    @PrePersist
    protected  void onCreate() {
        this.odate = LocalDateTime.now();
    }
}
