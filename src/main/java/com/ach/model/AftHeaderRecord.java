package com.ach.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.ach.dto.AftFileHeaderDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class AftHeaderRecord {
    private String institutionNumber; // 3-digit (e.g., 828)
    private String batchNumber;         // e.g., 00000001

    private String fileDate;

    public String toRecordString() {
        StringBuilder sb = new StringBuilder();
        sb.append("100");
        sb.append("C"); // Client file type
        sb.append(String.format("%-9s", institutionNumber)); // Institution number, padded
        sb.append("00000001"); // Batch number
        sb.append("BATCHHEADER");
        sb.append(String.format("%-25s", "")); // Reserved
        sb.append(fileDate);
        return sb.toString();
    }
}
