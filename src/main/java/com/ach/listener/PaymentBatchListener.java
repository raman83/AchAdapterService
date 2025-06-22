package com.ach.listener;

import com.ach.dto.*;
import com.ach.model.AftHeaderRecord;
import com.ach.service.AftBatchProcessor;
import com.ach.service.AftFileService;
import com.payment.event.PaymentBatchReadyEvent;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class PaymentBatchListener {


    private final AftBatchProcessor processor;

    @KafkaListener(topics = "payment.batch.ready", groupId = "ach-group", containerFactory = "kafkaListenerContainerFactory")
    public void handle(PaymentBatchReadyEvent event) {
        System.out.println("Consumed payment.batch.ready: " + event);

        // Convert to AftBatchRequest
        AftBatchRequest request = new AftBatchRequest();
        request.setHeader(new AftFileHeaderDto("828", event.getBatchId(), event.getTriggerTime()));

        List<AftPaymentRequest> payments = event.getPayments().stream().map(p ->
            new AftPaymentRequest(
                p.getSourceAccountId(),
                p.getDestinationAccount(),      // Dummy To Account
                mapBankToRouting(p.getDestinationBank()),            // Dummy routing
                p.getReceiverName(),
                p.getAmount(),
                "C",
                event.getTriggerTime()
            )
        ).toList();

        request.setPayments(payments);

        try {
            processor.process(request);
        } catch (Exception e) {
            System.err.println(" Error processing AFT file from event: " + e.getMessage());
        }
    }
    
    private String mapBankToRouting(String bankName) {
        return switch (bankName.toUpperCase()) {
            case "TD" -> "003000123";
            case "RBC" -> "002000456";
            case "BMO" -> "001000789";
            default -> "999999999"; // fallback
        };
    }
    
}
