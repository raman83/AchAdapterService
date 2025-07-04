package com.batch.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillPaymentRequest {
    private UUID paymentId;
    private String debtorAccount;
    private String billerName;
    private String billReferenceNumber;
    private BigDecimal amount;
    private String currency;
}