package com.tjg_project.candy.domain.order.repository;

import com.tjg_project.candy.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // ✅ 단순히 upk 기준으로 주문 조회
    List<Order> findByUpk(Long upk);
    // 주문 내역에서 삭제
    Optional<Order> findByOrderCode(String orderCode);
    Order findTop1ByUpk(Long upk);
    // ✅ 주문 + 상세(OrderDetail) 함께 조회 (지연 로딩 방지)
    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.orderDetails WHERE o.upk = :upk ORDER BY o.odate DESC")
    List<Order> findAllWithDetailsByUpk(@Param("upk") Long upk);
    Order findTop1ByUpkOrderByOdateDesc(Long upk);
}
