package com.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.product_service.entity.ProcessedEvent;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, Long> {
	
	boolean existsByOrderId(Long orderId);
	
	
	boolean existsByOrderIdAndEventType(Long orderId,String eventType);
	

}
