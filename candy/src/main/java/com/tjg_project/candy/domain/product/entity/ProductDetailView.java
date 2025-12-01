package com.tjg_project.candy.domain.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.jcip.annotations.Immutable;

import java.time.LocalDate;

@Entity
@Getter // getter생성
@Setter // setter생성
@NoArgsConstructor // 기본 생성자 생성
@Immutable // 뷰 전용(읽기만 가능, update, insert 불가)
@Table(name = "view_product_detail")
public class ProductDetailView {

    // product table
    @Id
    private Long id; // PK
    private String pid; // 상품 코드
    private String imageUrl; // 이미지
    private String imageUrlName; // 이미지URL이름
    private String brandName; // 브랜드명
    private String productName; // 상품명
    private int price; // 가격
    private String origin; // 원산지
    private String unit; // 판매단위
    private String weight; // 중량/용량
    private int count; // 재고 수량
    private int dc; // 할인율
    private String description; // 설명
    private boolean isHotDeal; // 핫딜 유무
    private boolean isMemberSpecial; // 멤버십 유무
    private String productDescriptionImage; // 상품 정보 이미지(상품 정보 탭)
    private String productInformationImage; // 상품 상세 정보 이미지(상품 상세 정보 탭)
    private LocalDate productDate; // 등록일
    private String seller; // 판매자 정보
    private String allergyInfo; // 알레르기 정보
    private String notes; // 안내 사항

    // category_sub table
    private Long categorySubId; // 카테고리 중분류
    // category_main table
    private Long categoryMainId; // 카테고리 대분류

    // delivery table
    private int delType; // 배송 타입
    private String delName; // 배송처
    private String delDescription; // 배송 정보
}
