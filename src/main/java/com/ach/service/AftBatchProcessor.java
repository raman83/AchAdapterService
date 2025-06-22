package com.ach.service;

import com.ach.dto.AftBatchRequest;
import com.ach.dto.AftSettlementTriggerEvent;
import com.ach.mapper.AftRecordMapper;
import com.ach.model.AftEntryRecord;
import com.ach.producer.SettlementEventProducer;
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
public class AftBatchProcessor {

    private final AftFileBuilder aftFileBuilder;
    private final SettlementEventProducer settlementEventProducer;

    public String process(AftBatchRequest request) throws IOException {
        List<AftEntryRecord> entries = request.getPayments().stream()
                .map(AftRecordMapper::toEntryRecord)
                .toList();

        String fileContent = aftFileBuilder.buildAftFile(entries, request.getHeader());
        String fileName = "AFT_" + UUID.randomUUID() + ".txt";

        Path outputDir = Path.of("batches");
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
}
