package com.ach.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class AftPaymentRequest {
    private String fromAccountNumber;
    private String toAccountNumber;
    private String toBankRoutingNumber;
    private String receiverName;
    private BigDecimal amount;
    private String type; // C = Credit, D = Debit
    private LocalDate effectiveDate;
}