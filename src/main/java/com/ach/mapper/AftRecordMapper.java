package com.ach.mapper;

import com.ach.dto.AftPaymentRequest;
import com.ach.model.AftEntryRecord;

public class AftRecordMapper {
    public static AftEntryRecord toEntryRecord(AftPaymentRequest request) {
        AftEntryRecord record = new AftEntryRecord();
        record.setTransactionType(request.getType());
        record.setFromAccountNumber(request.getFromAccountNumber());
        record.setToAccountNumber(request.getToAccountNumber());
        record.setToBankRoutingNumber(request.getToBankRoutingNumber());
        record.setReceiverName(request.getReceiverName());
        record.setAmount(request.getAmount());
        record.setEffectiveDate(request.getEffectiveDate());
        return record;
    }
}