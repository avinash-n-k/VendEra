package com.order_service.outbox;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order_service.entity.OutboxEvent;
import com.order_service.repository.OutboxEventRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OutboxPublisher {
	
	private OutboxEventRepository outboxEventRepo;
	private OutboxClaimService outboxClaimService;
	private OutboxStatusService outboxStatusService;
	private KafkaTemplate<String, byte[]> kafkaTemplate;
	private OutboxCleanupService outboxCleanupService;
	
	
	private static final long PROCESSING_TIMEOUT_MINUTES = 1;
	
//	@Scheduled(fixedDelay = 5000)
//	public void publishPendingEvent()
//	{
//		List<OutboxEvent> events=outboxEventRepo.findByStatus("PENDING");
//		
//		for(OutboxEvent event:events)
//		{
//			System.out.println("Publishing Outbox Event: "+event);
//			
//			
//			
//			kafkaTemplate.send(event.getTopic(),event.getPayload().getBytes(StandardCharsets.UTF_8)).whenComplete((result,ex)->{
//				if(ex==null)
//				{
//					System.out.println("OutBox Event Sent SuccessFully "+event.getId());
//					event.setStatus("PUBLISHED");
//					outboxEventRepo.save(event);
//				}
//				else
//				{
//					System.out.println("Failed to send Outbox event: " + event.getId());
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
			System.out.println("Publishing Outbox Event: "+event);
			
			boolean claimed=outboxClaimService.claimEvent(event.getId());
			
			if(claimed==false)
			{
				System.out.println("Event already claimed "+event.getId());
				continue;
			}
			else
			{
				kafkaTemplate.send(event.getTopic(),event.getPayload().getBytes(StandardCharsets.UTF_8)).whenComplete((result,ex)->{
					if(ex==null)
					{
						System.out.println("OutBox Event Sent SuccessFully "+event.getId());
//						event.setStatus("PUBLISHED");
//						outboxEventRepo.save(event);
						
						System.out.println(
						        "Calling markPublished for event: " + event.getId()
						    );

						    boolean status =outboxStatusService.markPublished(event.getId());

						    System.out.println(
						        "Rows updated by markPublished: " + status
						    );
					}
					else
					{
						System.out.println("Failed to send Outbox event: " + event.getId());
					}
				});
			}
			
			
		}
	}
	
	
	
	@Scheduled(fixedDelay = 10000)
	public void recoverStuckEvents()
	{
	    LocalDateTime cutoff =
	            LocalDateTime.now()
	                    .minusMinutes(PROCESSING_TIMEOUT_MINUTES);

	    List<OutboxEvent> events =outboxEventRepo.findByStatusAndProcessingStartedAtBefore("PROCESSING",cutoff);

	    for (OutboxEvent event : events)
	    {
	        event.setStatus("PENDING");
	        event.setProcessingStartedAt(null);

	        outboxEventRepo.save(event);

	        System.out.println(
	            "Recovered stuck Outbox Event: " + event.getId()
	        );
	    }
	}
	
	
	@Scheduled(fixedDelay = 300000)
	public void cleanupPublishedEvents()
	{
	    int deleted =
	            outboxCleanupService.deletePublishedEvents();

	    System.out.println(
	        "Deleted published Outbox events: " + deleted
	    );
	}
	

}
