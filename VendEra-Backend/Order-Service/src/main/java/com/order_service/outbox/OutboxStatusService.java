package com.order_service.outbox;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order_service.repository.OrderRepository;
import com.order_service.repository.OutboxEventRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OutboxStatusService {
	
	private OutboxEventRepository outboxEventRepo;
	
	@Transactional
	public boolean markPublished(Long id)
	{
		return outboxEventRepo.markPublished(id)==1;
	}
	
	

}
