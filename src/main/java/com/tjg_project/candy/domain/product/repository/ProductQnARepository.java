package com.tjg_project.candy.domain.product.repository;

import com.tjg_project.candy.domain.product.entity.Product;
import com.tjg_project.candy.domain.product.entity.ProductQnA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface ProductQnARepository extends JpaRepository<ProductQnA, Long>{
    @Query(value = """
            select pq.id,  pq.date, pq.is_private, pq.ppk, pq.status, pq.title, pq.upk, u.name as writer from  product_qna pq, users u where pq.upk = u.id
            """, nativeQuery = true)
    List<Map<String, Object>> findAllProductQnAWithUserName();
}
