package com.payment_service.outbox;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.payment_service.entity.OutboxEvent;
import com.payment_service.repository.OutboxEventRepository;

import lombok.AllArgsConstructor;



@Service
@AllArgsConstructor
public class OutboxPublisher {
	
	private OutboxCleanupService outboxCleanupService;
	private OutboxEventRepository outboxEventRepo;
	private KafkaTemplate<String, byte[]> kafkaTemplate;
	private OutboxClaimService outboxClaimService;
	private OutboxPublishedService outboxPublishedService;
	
	private static final long PROCESSING_TIMEOUT_MINUTES = 1;

	
	
//	public void publishPendingEvent()
//	{
//		List<OutboxEvent> events=outboxEventRepo.findByStatus("PENDING");
//		
//		for(OutboxEvent event:events)
//		{
//			kafkaTemplate.send(event.getTopic(),event.getPayload().getBytes(StandardCharsets.UTF_8)).whenComplete((result,ex)->{
//				if(ex==null) 
//				{
//					System.out.println("OutBox Event Sent SuccessFully "+event.getId()+" "+event.getTopic());
//					event.setStatus("PUBLISHED");
//					outboxEventRepo.save(event);
//				}
//				else
//				{
//					System.out.println("Failed to Send Event "+event.getId()+" "+event.getTopic());
//				}
//			});
//		}
//	}

	
	@Scheduled(fixedDelay = 5000)
	public void publishPendingEvent()
	{
		List<OutboxEvent> events=outboxEventRepo.findByStatus("PENDING");
		
		for(OutboxEvent event:events)
		{
			System.out.println("Publishing Outbox Event "+event.getId());
			
			int claimed=outboxClaimService.claimEvent(event.getId());
			
			if(claimed==0)
			{
				System.out.println("Event already Processed "+event.getId());
				continue;
			}
			
			kafkaTemplate.send(event.getTopic(), event.getPayload().getBytes(StandardCharsets.UTF_8)).whenComplete((result,ex)->{
				if(ex==null)
				{
					System.out.println("Processed SuccessFully ");
					outboxPublishedService.markPublished(event.getId());
				}
				else
				{
					System.out.println("Failed to Send Outbox Event "+event.getId());
				}
			});
		}
	}
	
	@Scheduled(fixedDelay = 10000)
	public void recoverStuckEvents()
	{
		LocalDateTime cutoff=LocalDateTime.now().minusMinutes(PROCESSING_TIMEOUT_MINUTES);
		
		List<OutboxEvent> events=outboxEventRepo.findByStatusAndProcessingStartedAtBefore("PROCESSING", cutoff);
		
		for(OutboxEvent event:events)
		{
			event.setStatus("PENDING");
			event.setProcessingStartedAt(null);
			outboxEventRepo.save(event);
		}
	}
	
	@Scheduled(fixedDelay = 300000)
	public void cleanUpPublishedEvents()
	{
		outboxCleanupService.cleanUp();
		
	}
}
