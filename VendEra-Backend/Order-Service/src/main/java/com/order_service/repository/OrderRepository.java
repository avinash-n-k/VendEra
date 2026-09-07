package com.order_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.order_service.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	
	List<Order> findByUserEmail(String email);
	
	@Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items WHERE o.id = :orderId")
    Order findOrderWithItems(Long orderId);
}
