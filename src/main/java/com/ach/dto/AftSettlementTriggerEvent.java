package com.ach.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AftSettlementTriggerEvent {
    private String fileName;
    private LocalDate batchDate;
    private int recordCount;
    private String eventType = "payment.ready-for-settlement";
}