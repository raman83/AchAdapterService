package com.ach.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.ach.dto.AftFileHeaderDto;

import lombok.Data;
@Data

public class AftHeaderRecord {
    private String institutionNumber; // 3-digit (e.g., 828)
    private LocalDate fileDate;

    public String toRecordString() {
        StringBuilder sb = new StringBuilder();
        sb.append("100");
        sb.append("C"); // Client file type
        sb.append(String.format("%-9s", institutionNumber)); // Institution number, padded
        sb.append("00000001"); // Batch number
        sb.append("BATCHHEADER");
        sb.append(String.format("%-25s", "")); // Reserved
        sb.append(fileDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        return sb.toString();
    }
}
