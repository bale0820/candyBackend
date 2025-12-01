package com.tjg_project.candy.domain.product.repository;

import com.tjg_project.candy.domain.product.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = """
    SELECT 
        p.id,
        p.brand_name,
        p.dc,
        p.description,
        p.image_url,
        p.image_url_name,
        p.is_hot_deal,
        p.is_member_special,
        p.origin,
        p.pid,
        p.price,
        p.product_date,
        p.product_description_image,
        p.product_information_image,
        p.product_name
    FROM orders o
    JOIN product p ON o.ppk = p.id
    GROUP BY p.id
    ORDER BY SUM(o.qty) DESC
    LIMIT 10
""", nativeQuery = true)
    List<Product> findProductBestList();

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.count = p.count - :qty WHERE p.id = :id")
    public int decreaseCount(@Param("id") Long id, @Param("qty") Long qty);
}
