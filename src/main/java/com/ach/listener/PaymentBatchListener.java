package com.ach.listener;

import com.ach.model.AftHeaderRecord;
import com.ach.service.BatchProcessor;
import com.batch.dto.*;
import com.payment.event.BillPaymentBatchReadyEvent;
import com.payment.event.PaymentBatchReadyEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentBatchListener {


    private final BatchProcessor processor;

    @KafkaListener(topics = "payment.batch.ready", groupId = "ach-adapter-group", containerFactory = "kafkaListenerContainerFactory")
    public void handle(PaymentBatchReadyEvent event) {
        System.out.println("Consumed payment.batch.ready: " + event);

        // Convert to AftBatchRequest
        AftBatchRequest request = new AftBatchRequest();
        request.setHeader(new AftFileHeaderDto("828", event.getBatchId(), event.getTriggerTime()));

        List<AftPaymentRequest> payments = event.getPayments().stream().map(p ->
            new AftPaymentRequest(
                p.getDebtorAccount(),
                p.getCreditorAccount(),      // Dummy To Account
                mapBankToRouting(p.getCreditorBank()),            // Dummy routing
                p.getCreditorName(),
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
    
    
    
    
    @KafkaListener(topics = "bill.payment.batch.ready", groupId = "ach-group", containerFactory = "kafkaListenerContainerFactory")
    public void handleBillPayment(BillPaymentBatchReadyEvent event) {
        System.out.println("Consumed payment.batch.ready: " + event);

     // 1. Create file header
        BillFileHeaderDto header = BillFileHeaderDto.builder()
                .originatorId("EQ_BANK")
                .fileDate(event.getTriggerTime())
                .currency("CAD")
                .batchType("BILLPAY")
                .build();

        // 2. Map CanonicalPayments to BillPaymentRequest
        List<BillPaymentRequest> payments = event.getPayments().stream()
                .map(p -> BillPaymentRequest.builder()
                        .paymentId(p.getPaymentId())
                        .debtorAccount(p.getDebtorAccount())
                        .billerName(p.getCreditorName())
                        .billReferenceNumber(p.getPurpose()) // assuming `purpose` holds the reference
                        .amount(p.getAmount())
                        .currency(p.getCurrency())
                        .build())
                .toList();

        // 3. Build request
        BillBatchRequest request = BillBatchRequest.builder()
                .header(header)
                .payments(payments)
                .build();

        // 4. Process the batch
        try {
            processor.processBillBatch(request);
        } catch (Exception e) {
            log.error("❌ Failed to process BILL file: {}", e.getMessage(), e);
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
