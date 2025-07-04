package com.ach.mapper;
import com.common.iso.CanonicalPayment;
import com.batch.dto.BillPaymentRequest;

public class BillRecordMapper {

    public static BillPaymentRequest toEntryRecord(CanonicalPayment payment) {
        return BillPaymentRequest.builder()
                .paymentId(payment.getPaymentId())
                .debtorAccount(payment.getDebtorAccount())
                .billerName(payment.getCreditorName())
                .billReferenceNumber(payment.getPurpose())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .build();
    }
}