package com.ach.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.payment.event.PaymentBatchReadyEvent;

@Service
public class AftFileService {

    public void generateAndSendAftFile(PaymentBatchReadyEvent event) {
        var header = new StringBuilder()
            .append("InstitutionNumber: 828\n")
            .append("BatchNumber: ").append(event.getBatchId()).append("\n")
            .append("FileDate: ").append(LocalDate.now()).append("\n");

        var body = new StringBuilder();
        event.getPayments().forEach(payment -> {
            body.append("From: ").append(payment.getSourceAccountId()).append("\n")
                .append(		"To: ").append(payment.getDestinationAccount()).append("\n")
                .append("Purpose: ").append(payment.getPurpose()).append("\n")
                .append("Amount: ").append(payment.getAmount()).append("\n")
                .append("Receiver").append(payment.getReceiverName()).append("\n")
                .append("EffectiveDate: ").append(LocalDate.now()).append("\n")
                .append("---\n");
        });

        var fileContent = header.append("\n").append(body).toString();
        System.out.println("Generated AFT file:\n" + fileContent);

        // TODO: Call settlement-service HTTP endpoint with this data as JSON
    }
}
