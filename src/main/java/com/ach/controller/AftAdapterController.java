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
import com.ach.service.AftBatchProcessor;
import com.ach.service.AftFileBuilder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/aft")
public class AftAdapterController {

    private final AftBatchProcessor processor;

    @PostMapping("/generate")
    public ResponseEntity<String> generateAftFile(@RequestBody AftBatchRequest request) {
        try {
            String fileContent = processor.process(request);
            return ResponseEntity.ok(fileContent);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File write error");
        }
    }
}