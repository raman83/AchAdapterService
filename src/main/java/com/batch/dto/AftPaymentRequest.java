package com.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AftPaymentRequest {
    private String fromAccountNumber;
    private String toAccountNumber;
    private String toBankRoutingNumber;
    private String receiverName;
    private BigDecimal amount;
    private String type;
    private String effectiveDate;
}
