package com.ach.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.batch.dto.AftFileHeaderDto;

import lombok.Data;

@Data

public class AftEntryRecord {
    private String transactionType;         // C = Credit, D = Debit
    private String fromAccountNumber;
    private String toAccountNumber;
    private String toBankRoutingNumber;     // Format: Institution (3 digits) + Branch (5 digits)
    private BigDecimal amount;              // In dollars
    private String receiverName;
    private String effectiveDate;

    public String toRecordString() {
        StringBuilder sb = new StringBuilder();

        sb.append("200"); // Record type
        sb.append(transactionType); // C or D
        sb.append(String.format("%-15s", fromAccountNumber)); // pad to 15
        sb.append(String.format("%-15s", toAccountNumber));   // pad to 15
        sb.append(String.format("%9s", toBankRoutingNumber)); // 3 (inst) + 5 (branch) = 8, pad to 9
        sb.append(String.format("%010d", amount.movePointRight(2).intValue())); // amount in cents, 10 chars
        sb.append(String.format("%-20s", receiverName)); // Receiver name, padded
        sb.append(effectiveDate); // Effective date

        return sb.toString();
    }
}
