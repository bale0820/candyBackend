package com.tjg_project.candy.domain.product.repository;

import com.tjg_project.candy.domain.product.entity.ProductDetailView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailViewRepository extends JpaRepository<ProductDetailView, Long> {
}
