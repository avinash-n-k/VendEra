package com.order_service.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.order_service.entity.OutboxEvent;



public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
	
	

	List<OutboxEvent> findByStatus(String status);
	
	
//	@Modifying
//	@NativeQuery(value = "UPDATE OUTBOXEVENTS E SET E.STATUS='PROCESSING' WHERE E.ID=?ID AND E.STATUS='PENDING'")
//	int claimEvent(Long id);
	
	
	@Modifying
	@Query(
	    value = "UPDATE outbox_events SET status = 'PROCESSING',processing_started_at = CURRENT_TIMESTAMP WHERE id = :id AND status = 'PENDING'",
	    nativeQuery = true
	)
	int claimEvent(@Param("id") Long id);
	
	
	List<OutboxEvent> findByStatusAndProcessingStartedAtBefore(
	        String status,
	        LocalDateTime time
	);
	
	
	@Modifying
	@Query("""
	    UPDATE OutboxEvent e
	    SET e.status = 'PUBLISHED'
	    WHERE e.id = :id
	""")
	int markPublished(@Param("id") Long id);
	
	
	@Modifying
	@Query("""
	    DELETE FROM OutboxEvent e
	    WHERE e.status = 'PUBLISHED'
	""")
	int deletePublishedEvents();
	
	
	
	
}
