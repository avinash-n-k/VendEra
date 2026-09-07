package com.order_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "outbox_events")
public class OutboxEvent {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String eventType;
	private String topic;
	private Long aggregateId;
	
	@Column(columnDefinition = "TEXT")
	private String payload;
	
	private String status;
	
	private LocalDateTime processingStartedAt;
}
