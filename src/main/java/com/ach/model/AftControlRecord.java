package com.ach.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.batch.dto.AftFileHeaderDto;

import lombok.Data;
@Data

public class AftControlRecord {
    private int recordCount;
    private BigDecimal totalAmount;

    public String toRecordString() {
        StringBuilder sb = new StringBuilder();
        sb.append("900"); // Record type
        sb.append(String.format("%07d", recordCount)); // padded count
        sb.append(String.format("%013d", totalAmount.movePointRight(2).intValue())); // amount in cents
        sb.append("0000001"); // Batch count
        sb.append("BATCHCONTROL");
        return sb.toString();
    }
}
