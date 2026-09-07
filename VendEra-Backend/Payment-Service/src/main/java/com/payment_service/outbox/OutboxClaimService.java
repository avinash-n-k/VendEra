package com.payment_service.outbox;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.payment_service.repository.OutboxEventRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OutboxClaimService {
	
	private OutboxEventRepository outboxEventRepo;
	
	@Transactional
	public int claimEvent(Long id)
	{
		return outboxEventRepo.claimEvent(id);
	}

}
