package com.tjg_project.candy.domain.order.repository;

import com.tjg_project.candy.domain.order.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // ✅ fetch join으로 Cart + Product를 한 번에 가져오기
    @Query("SELECT c FROM Cart c JOIN FETCH c.product WHERE c.cid IN :cidList")
    List<Cart> findAllWithProductByCidIn(@Param("cidList") List<Long> cidList);

    // ✅ 선택된 장바구니 항목 조회 (결제용)
    List<Cart> findByCidIn(List<Long> cidList);

    // ✅ 특정 사용자(upk)의 장바구니 조회
    List<Cart> findByUser_Id(Long upk);

    // ✅ 결제 완료 후 장바구니 비우기
    void deleteByUser_Id(Long upk);

    // 장바구니에 존재 유무 확인
    Optional<Cart> findByUser_IdAndProduct_Id(Long uid, Long pid);
}
