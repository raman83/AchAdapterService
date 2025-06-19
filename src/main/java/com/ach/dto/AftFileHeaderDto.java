package com.ach.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AftFileHeaderDto {
    private String institutionNumber;   // e.g., 828
    private String batchNumber;         // e.g., 00000001
    private LocalDate fileDate;         // Effective date
}
