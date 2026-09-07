package com.product_service.outbox;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product_service.repository.OutboxEventRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OutboxCleanupService {
	
	private OutboxEventRepository outboxEventRepo;
	
	@Transactional
	public int cleanUp()
	{
		return outboxEventRepo.deletePublishedEvents();
	}
	
}
