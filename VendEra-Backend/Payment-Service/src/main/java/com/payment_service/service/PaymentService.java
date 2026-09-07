package com.payment_service.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.payment_service.dto.PaymentFailedRequest;
import com.payment_service.dto.VerifyPaymentRequest;
import com.payment_service.entity.OutboxEvent;
import com.payment_service.entity.Payment;
import com.payment_service.kafka.PaymentFailedEvent;
import com.payment_service.kafka.PaymentFailedItemEvent;
import com.payment_service.kafka.PaymentProcessedEvent;
import com.payment_service.kafka.PaymentProducer;
import com.payment_service.kafka.StockReservedEvent;
import com.payment_service.kafka.StockReservedItemEvent;
import com.payment_service.repository.OutboxEventRepository;
import com.payment_service.repository.PaymentRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentProducer pProducer;
    private final PaymentRepository pRepo;
    private final ObjectMapper objectMapper;
    private final OutboxEventRepository outboxEventRepo;
    private final RazorpayClient razorpayClient;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

//	@Transactional
//	public void processPayment(StockReservedEvent event) throws Exception
//	{
//		Payment payment=new Payment();
//		try
//		{
//			
////			payment.setOrderId(event.getOrderId());
////			payment.setAmount(event.getAmount());
//////			throw new RuntimeException("Payment Processing Failed");
////			payment.setStatus("SUCCESS");
////			
////			pRepo.save(payment);
//			/**********************************************************************/
//			
//			payment.setOrderId(event.getOrderId());
//			payment.setAmount(event.getAmount());
//			payment.setStatus("PENDING");
//
//			pRepo.save(payment);
//
//			String razorpayOrderId =createRazorpayOrder(payment);
//
//			payment.setRazorpayOrderId(razorpayOrderId);
//
//			pRepo.save(payment);
//			
//			/**********************************************************************/
//			
//			
//			
//			
//			
////			PaymentProcessedEvent paymentprocessedevent=new PaymentProcessedEvent();
////			paymentprocessedevent.setOrderId(event.getOrderId());
////			paymentprocessedevent.setProductId(event.getProductId());
////
////
////			
////			String payload=objectMapper.writeValueAsString(paymentprocessedevent);
////			
////			OutboxEvent outboxevent=new OutboxEvent();
////			outboxevent.setAggregateId(event.getOrderId());
////			outboxevent.setEventType("PaymentProcessedEvent");
////			outboxevent.setStatus("PENDING");
////			outboxevent.setPayload(payload);
////			outboxevent.setTopic("payment-processed-events");
////			
////			outboxEventRepo.save(outboxevent);
//			
//			
////			pProducer.sendPaymentProcessedEvent(paymentprocessedevent);
//		}
//		catch(Exception ex)
//		{
//			payment.setOrderId(event.getOrderId());
//			payment.setAmount(event.getAmount());
//			payment.setStatus("FAILED");
//			pRepo.save(payment);
//			
//			PaymentFailedEvent paymentfailedevent=new PaymentFailedEvent();
//			
//			paymentfailedevent.setOrderId(event.getOrderId());
//			paymentfailedevent.setProductId(event.getProductId());
//			paymentfailedevent.setQuantity(event.getQuantity());
//			
//			
//			String payload=objectMapper.writeValueAsString(paymentfailedevent);
//			OutboxEvent outboxevent=new OutboxEvent();
//			
//			outboxevent.setAggregateId(event.getOrderId());
//			outboxevent.setEventType("PaymentFailedEvent");
//			outboxevent.setStatus("PENDING");
//			outboxevent.setPayload(payload);
//			outboxevent.setTopic("payment-failed-events");
//			
//			outboxEventRepo.save(outboxevent);
//		
////			pProducer.sendPaymentFailedEvent(paymentfailedevent);
//		}
//	}
	
	
	
	@Transactional
	public void processPayment(StockReservedEvent event) throws Exception
	{
		Payment payment=new Payment();
		try
		{
			
//			payment.setOrderId(event.getOrderId());
//			payment.setAmount(event.getAmount());
////			throw new RuntimeException("Payment Processing Failed");
//			payment.setStatus("SUCCESS");
//			
//			pRepo.save(payment);
			/**********************************************************************/
			
			payment.setOrderId(event.getOrderId());

			double totalAmount = 0.0;

			for (StockReservedItemEvent item : event.getItems())
			{
			    totalAmount = totalAmount + item.getAmount();
			}

			payment.setAmount(totalAmount);
			payment.setStatus("PENDING");
			String itemsJson = objectMapper.writeValueAsString(event.getItems());
			payment.setItems(itemsJson);
			pRepo.save(payment);

//			if(true)
//			{
//			throw new RuntimeException("Payment Processing Failed");
//			}
			String razorpayOrderId =createRazorpayOrder(payment);

			payment.setRazorpayOrderId(razorpayOrderId);

			pRepo.save(payment);
			
			/**********************************************************************/
			
			
			
			
			
//			PaymentProcessedEvent paymentprocessedevent=new PaymentProcessedEvent();
//			paymentprocessedevent.setOrderId(event.getOrderId());
//			
//
//
//			
//			String payload=objectMapper.writeValueAsString(paymentprocessedevent);
//			
//			OutboxEvent outboxevent=new OutboxEvent();
//			outboxevent.setAggregateId(event.getOrderId());
//			outboxevent.setEventType("PaymentProcessedEvent");
//			outboxevent.setStatus("PENDING");
//			outboxevent.setPayload(payload);
//			outboxevent.setTopic("payment-processed-events");
//			
//			outboxEventRepo.save(outboxevent);
			
			
//			pProducer.sendPaymentProcessedEvent(paymentprocessedevent);
		}
		catch(Exception ex)
		{
			payment.setOrderId(event.getOrderId());
			double totalAmount = 0.0;

			for (StockReservedItemEvent item : event.getItems())
			{
			    totalAmount = totalAmount + item.getAmount();
			}

			payment.setAmount(totalAmount);
			payment.setStatus("FAILED");
			pRepo.save(payment);
			
			PaymentFailedEvent paymentfailedevent=new PaymentFailedEvent();
			
			paymentfailedevent.setOrderId(event.getOrderId());
			
			List<PaymentFailedItemEvent> failedItems = new ArrayList<>();

			for (StockReservedItemEvent item : event.getItems())
			{
			    PaymentFailedItemEvent failedItem = new PaymentFailedItemEvent();

			    failedItem.setProductId(item.getProductId());
			    failedItem.setQuantity(item.getQuantity());

			    failedItems.add(failedItem);
			}

			paymentfailedevent.setItems(failedItems);
			
			
			String payload=objectMapper.writeValueAsString(paymentfailedevent);
			OutboxEvent outboxevent=new OutboxEvent();
			
			outboxevent.setAggregateId(event.getOrderId());
			outboxevent.setEventType("PaymentFailedEvent");
			outboxevent.setStatus("PENDING");
			outboxevent.setPayload(payload);
			outboxevent.setTopic("payment-failed-events");
			
			outboxEventRepo.save(outboxevent);
		
//			pProducer.sendPaymentFailedEvent(paymentfailedevent);
		}
	}
	
	
	public String createRazorpayOrder(Payment payment) throws Exception {

	    long amountInPaise =
	            Math.round(payment.getAmount() * 100);

	    JSONObject orderRequest = new JSONObject();
	    

	    orderRequest.put("amount", amountInPaise);
	    orderRequest.put("currency", "INR");
	    orderRequest.put("receipt", "order_" + payment.getOrderId());

	    com.razorpay.Order razorpayOrder =
	            razorpayClient.orders.create(orderRequest);

	    return razorpayOrder.get("id");
	}
	
	
//	public Payment getPaymentByOrderId(Long orderId) {
//	    return pRepo.findByOrderId(orderId)
//	            .orElseThrow(() -> new RuntimeException("Payment not found"));
//	}
	
	public Payment getPaymentByOrderId(Long orderId) {
	    return pRepo.findByOrderId(orderId).orElseThrow(() ->new ResponseStatusException(
	                    HttpStatus.NOT_FOUND,
	                    "Payment not found"
	                )
	            );
	}
	
	
	public Payment verifyPayment(VerifyPaymentRequest request) throws Exception {

	    Payment payment = pRepo.findByOrderId(request.getOrderId())
	            .orElseThrow(() -> new ResponseStatusException(
	                    HttpStatus.NOT_FOUND,
	                    "Payment not found"
	            ));

	    
	    //Frontend linda barooo Razorpay id Trust madd byad paa Huli alee proprties file dag ite nod adarle complare madd paa 
	    if (!payment.getRazorpayOrderId()
	            .equals(request.getRazorpayOrderId())) {

	        throw new ResponseStatusException(
	                HttpStatus.BAD_REQUEST,
	                "Razorpay Order ID mismatch"
	        );
	    }

	    JSONObject options = new JSONObject();

	    options.put(
	            "razorpay_order_id",
	            payment.getRazorpayOrderId()
	    );

	    options.put(
	            "razorpay_payment_id",
	            request.getRazorpayPaymentId()
	    );

	    options.put(
	            "razorpay_signature",
	            request.getRazorpaySignature()
	    );

	    boolean verified = Utils.verifyPaymentSignature(
	            options,
	            razorpayKeySecret
	    );

	    if (!verified) {

	        throw new ResponseStatusException(
	                HttpStatus.BAD_REQUEST,
	                "Payment signature verification failed"
	        );
	    }

	    payment.setRazorpayPaymentId(
	            request.getRazorpayPaymentId()
	    );

	    payment.setStatus("SUCCESS");

	    pRepo.save(payment);
	    
	    


	    /**********************************************************************/

	    PaymentProcessedEvent paymentprocessedevent = new PaymentProcessedEvent();

	    paymentprocessedevent.setOrderId(request.getOrderId());

	    String payload = objectMapper.writeValueAsString(
	            paymentprocessedevent
	    );

	    OutboxEvent outboxevent = new OutboxEvent();

	    outboxevent.setAggregateId(request.getOrderId());
	    outboxevent.setEventType("PaymentProcessedEvent");
	    outboxevent.setStatus("PENDING");
	    outboxevent.setPayload(payload);
	    outboxevent.setTopic("payment-processed-events");

	    outboxEventRepo.save(outboxevent);

	    /**********************************************************************/

	    return payment;
	}
	
	
	
	@Transactional
	public Payment failPayment(PaymentFailedRequest request) throws Exception {

	    Payment payment = pRepo.findByOrderId(request.getOrderId())
	            .orElseThrow(() -> new ResponseStatusException(
	                    HttpStatus.NOT_FOUND,
	                    "Payment not found"
	            ));

	    // nod paaa yala match akaab ilaa anthaa
	    if (request.getRazorpayOrderId() != null
	            && !request.getRazorpayOrderId()
	                    .equals(payment.getRazorpayOrderId())) {

	        throw new ResponseStatusException(
	                HttpStatus.BAD_REQUEST,
	                "Razorpay Order ID mismatch"
	        );
	    }

	    // one satha fail agaith andramuguit
	    if ("FAILED".equals(payment.getStatus())) {
	        return payment;
	    }

	    // paymnet dag items irbek  bahi
	    if (payment.getItems() == null) {
	        throw new ResponseStatusException(
	                HttpStatus.INTERNAL_SERVER_ERROR,
	                "Payment item information not found"
	        );
	    }

	    payment.setStatus("FAILED");

	    if (request.getRazorpayPaymentId() != null) {
	        payment.setRazorpayPaymentId(
	                request.getRazorpayPaymentId()
	        );
	    }

	    pRepo.save(payment);

	    // Reconstruct the original reserved items
	    List<StockReservedItemEvent> reservedItems =
	            objectMapper.readValue(
	                    payment.getItems(),
	                    new TypeReference<List<StockReservedItemEvent>>() {}
	            );

	    PaymentFailedEvent paymentFailedEvent =
	            new PaymentFailedEvent();

	    paymentFailedEvent.setOrderId(payment.getOrderId());

	    List<PaymentFailedItemEvent> failedItems =
	            new ArrayList<>();

	    for (StockReservedItemEvent item : reservedItems) {

	        PaymentFailedItemEvent failedItem =
	                new PaymentFailedItemEvent();

	        failedItem.setProductId(item.getProductId());
	        failedItem.setQuantity(item.getQuantity());

	        failedItems.add(failedItem);
	    }

	    paymentFailedEvent.setItems(failedItems);

	    String payload =
	            objectMapper.writeValueAsString(paymentFailedEvent);

	    OutboxEvent outboxEvent = new OutboxEvent();

	    outboxEvent.setAggregateId(payment.getOrderId());
	    outboxEvent.setEventType("PaymentFailedEvent");
	    outboxEvent.setStatus("PENDING");
	    outboxEvent.setPayload(payload);
	    outboxEvent.setTopic("payment-failed-events");

	    outboxEventRepo.save(outboxEvent);

	    return payment;
	}
	
	public List<Payment> getAllPayments() {
	    return pRepo.findAll();
	}
}
