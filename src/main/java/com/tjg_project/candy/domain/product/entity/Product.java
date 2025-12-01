package com.tjg_project.candy.domain.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tjg_project.candy.domain.category.entity.CategorySub;
import com.tjg_project.candy.domain.user.entity.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Column(length = 20, nullable = false, unique = true,
            insertable = false, updatable = false)
    private String pid; // 상품 코드

    @Column(length = 255)
    private String imageUrl;

    @Column(name = "image_url_name", length = 100)
    private String imageUrlName;

    @Column(length = 100)
    private String brandName;

    @Column(length = 200)
    private String productName;

    private int price;

    @Column(length = 100)
    private String origin; // 원산지

    private String unit; // 판매단위

    private String weight; // 중량/용량

    private int count; // 재고 수량

    private int dc; // 할인율

    @Column(length = 500)
    private String description; // 상품 설명

    private boolean isHotDeal;

    private boolean isMemberSpecial;

    private int delType;

    @Column(length = 255)
    private String productDescriptionImage;

    @Column(length = 255)
    private String productInformationImage;

    @Column(length = 30)
    private LocalDate productDate;

    private String seller; // 판매자 정보

    private String allergyInfo; // 알레르기 정보

    private String notes; // 안내 사항

    // ✅ 사용자 (users.id = upk)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upk", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Users user;

    // 카테고리 중분류 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_sub_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private CategorySub categorySub;
}
