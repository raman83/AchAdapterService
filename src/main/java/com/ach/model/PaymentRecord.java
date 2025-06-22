package com.ach.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRecord {
    private String sourceAccountId;
    private String destinationAccount;
    private BigDecimal amount;
    private String purpose;
    private String destinationBank;
    private String currency;
    private String receiverName;

}
