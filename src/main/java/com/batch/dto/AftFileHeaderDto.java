package com.batch.dto;

import java.time.LocalDate;

import com.ach.service.BatchProcessor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class AftFileHeaderDto {
    private String institutionNumber;   // e.g., 828
    private String batchNumber;         // e.g., 00000001
    private String fileDate;         // Effective date
}
