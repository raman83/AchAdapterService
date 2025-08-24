package com.ach.service;

import com.ach.mapper.AftRecordMapper;
import com.ach.mapper.BillRecordMapper;
import com.ach.model.AftEntryRecord;
import com.ach.producer.SettlementEventProducer;
import com.batch.dto.AftBatchRequest;
import com.batch.dto.AftSettlementTriggerEvent;
import com.batch.dto.BillBatchRequest;
import com.batch.dto.BillPaymentRequest;
import com.batch.dto.BillSettlementTriggerEvent;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BatchProcessor {

    private final AftFileBuilder aftFileBuilder;
    private final BillFileBuilder billFileBuilder;

    private final SettlementEventProducer settlementEventProducer;

    public String process(AftBatchRequest request) throws IOException {
        List<AftEntryRecord> entries = request.getPayments().stream()
                .map(AftRecordMapper::toEntryRecord)
                .toList();

        String fileContent = aftFileBuilder.buildAftFile(entries, request.getHeader());
        String fileName = "AFT_" + UUID.randomUUID() + ".txt";

       // Path outputDir = Path.of("/Users/reyansh/Desktop/raman/Auth/batch");
        Path outputDir = Path.of("/Users/reyansh/Desktop/raman/Auth/batch");

        Files.createDirectories(outputDir);
        Files.writeString(outputDir.resolve(fileName), fileContent);

        // Delayed async event
        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            AftSettlementTriggerEvent event = new AftSettlementTriggerEvent(
                fileName,
                request.getHeader().getFileDate(),
                entries.size(),
                "payment.ready-for-settlement"
            );
            settlementEventProducer.publish(event);
            System.out.println("Published payment.ready-for-settlement event: " + event);
        }, 5, TimeUnit.SECONDS);

        return fileContent;
    }
    
    
    public String processBillBatch(BillBatchRequest request) throws IOException {
        List<BillPaymentRequest> entries = request.getPayments();
        
        
        String fileContent = billFileBuilder.buildFile(entries, request.getHeader());
        String fileName = "BILL_" + UUID.randomUUID() + ".txt";

        Path outputDir = Path.of("/Users/reyansh/Desktop/raman/Auth/batch");
        Files.createDirectories(outputDir);
        Files.writeString(outputDir.resolve(fileName), fileContent);

            BillSettlementTriggerEvent event = new BillSettlementTriggerEvent(
                fileName,
                request.getHeader().getFileDate(),
                entries.size(),
                "billpayment.ready-for-settlement"
            );
            settlementEventProducer.publishBill(event);
      

        return fileContent;
    }
    
}
