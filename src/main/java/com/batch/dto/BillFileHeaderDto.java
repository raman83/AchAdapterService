package com.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillFileHeaderDto {
    private String originatorId;        // e.g., EQ_BANK
    private String fileDate;         // For settlement reference
    private String currency;            // Usually CAD
    private String batchType;           // Optional: e.g., “BILLPAY”
}
