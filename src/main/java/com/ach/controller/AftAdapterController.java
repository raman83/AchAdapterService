package com.ach.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ach.dto.AftBatchRequest;
import com.ach.dto.AftSettlementTriggerEvent;
import com.ach.mapper.AftRecordMapper;
import com.ach.model.AftEntryRecord;
import com.ach.producer.SettlementEventProducer;
import com.ach.service.AftFileBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/aft")
@RequiredArgsConstructor
public class AftAdapterController {

    private final AftFileBuilder aftFileBuilder;
    private final SettlementEventProducer settlementEventProducer;

    @PostMapping("/generate")
    public ResponseEntity<String> generateAftFile(@RequestBody AftBatchRequest request) {
        List<AftEntryRecord> entries = request.getPayments().stream()
                .map(AftRecordMapper::toEntryRecord)
                .toList();

        String fileContent = aftFileBuilder.buildAftFile(entries, request.getHeader());
        String fileName = "AFT_" + UUID.randomUUID() + ".txt";

        try {
            Path outputDir = Path.of("batches");
            Files.createDirectories(outputDir);
            Files.writeString(outputDir.resolve(fileName), fileContent);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File write error");
        }

        // Simulate delayed publishing of settlement trigger event
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> {
            AftSettlementTriggerEvent event = new AftSettlementTriggerEvent(
                fileName,
                request.getHeader().getFileDate(),
                entries.size(),"payment.ready-for-settlement"
            );
            settlementEventProducer.publish(event);
            System.out.println("✅ Published payment.ready-for-settlement event: " + event);
        }, 5, TimeUnit.SECONDS);

        return ResponseEntity.ok(fileContent);
    }
}
