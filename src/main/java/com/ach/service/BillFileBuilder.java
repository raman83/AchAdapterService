package com.ach.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.batch.dto.BillFileHeaderDto;
import com.batch.dto.BillPaymentRequest;

import java.util.List;
import java.util.StringJoiner;

@Component
@Slf4j
public class BillFileBuilder {

    public String buildFile(List<BillPaymentRequest> records, BillFileHeaderDto header) {
        StringBuilder builder = new StringBuilder();

        // Header line
        builder.append(buildHeaderLine(header)).append("\n");

        // Detail lines
        for (BillPaymentRequest record : records) {
            builder.append(buildRecordLine(record)).append("\n");
        }

        // Trailer line
        builder.append(buildTrailerLine(records.size(), records)).append("\n");

        return builder.toString();
    }

    private String buildHeaderLine(BillFileHeaderDto header) {
        return String.format("HDR|%s|%s|%s|%s",
                header.getOriginatorId(),
                header.getBatchType(),
                header.getFileDate(),
                header.getCurrency()
        );
    }

    private String buildRecordLine(BillPaymentRequest record) {
        return String.format("REC|%s|%s|%s|%s|%s|%s",
                record.getPaymentId(),
                record.getDebtorAccount(),
                record.getBillerName(),
                record.getBillReferenceNumber(),
                record.getAmount(),
                record.getCurrency()
        );
    }

    private String buildTrailerLine(int recordCount, List<BillPaymentRequest> records) {
        StringJoiner ids = new StringJoiner(",");
        records.forEach(r -> ids.add(r.getPaymentId().toString()));

        return String.format("TRL|%d|%s", recordCount, ids);
    }
}
