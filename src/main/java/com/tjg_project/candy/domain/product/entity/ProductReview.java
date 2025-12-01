package com.tjg_project.candy.domain.product.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "reviews")
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ppk; // Product.id 참조

    // 유저 테이블의 id (User.id)
    @Column(nullable = false)
    private Long upk; // User.id 참조

    @Column(length = 200)
    private String productName;

    @Column(length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 20)
    private String date;

    private boolean isBest;

    private int likes;

    @Column(columnDefinition = "JSON")
    private String tags; // JSON 문자열 형태 ["매콤달콤","밥도둑"]

    @Column(columnDefinition = "JSON")
    private String images; // JSON 문자열 형태 ["/img1.png", "/img2.png"]
}
