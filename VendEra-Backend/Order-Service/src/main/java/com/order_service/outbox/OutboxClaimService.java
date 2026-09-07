package com.order_service.outbox;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order_service.repository.OutboxEventRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor

public class OutboxClaimService {

    private OutboxEventRepository outboxEventRepo;

    @Transactional
    public boolean claimEvent(Long id)
    {
        return outboxEventRepo.claimEvent(id) == 1;
    }
}
