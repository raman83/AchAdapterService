package com.ach.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ach.dto.AftBatchRequest;
import com.ach.mapper.AftRecordMapper;
import com.ach.model.AftEntryRecord;
import com.ach.service.AftFileBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/aft")
@RequiredArgsConstructor
public class AftAdapterController {

    private final AftFileBuilder aftFileBuilder;

    @PostMapping("/generate")
    public ResponseEntity<String> generateAftFile(@RequestBody AftBatchRequest request) {
        List<AftEntryRecord> entries = request.getPayments().stream()
                .map(AftRecordMapper::toEntryRecord)
                .toList();

        String fileContent = aftFileBuilder.buildAftFile(entries, request.getHeader());

        try {
            String filename = "AFT_" + UUID.randomUUID() + ".txt";
            Path outputDir = Path.of("batches");
            Files.createDirectories(outputDir);
            Files.writeString(outputDir.resolve(filename), fileContent);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File write error");
        }

        return ResponseEntity.ok(fileContent);
    }
}
