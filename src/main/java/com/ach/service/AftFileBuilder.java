package com.ach.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ach.dto.AftFileHeaderDto;
import com.ach.model.AftControlRecord;
import com.ach.model.AftEntryRecord;
import com.ach.model.AftHeaderRecord;

@Service
public class AftFileBuilder {

    public String buildAftFile(List<AftEntryRecord> entries, AftFileHeaderDto headerDto) {
        AftHeaderRecord header = new AftHeaderRecord();
        header.setInstitutionNumber(headerDto.getInstitutionNumber());
        header.setFileDate(headerDto.getFileDate());

        AftControlRecord control = new AftControlRecord();
        control.setRecordCount(entries.size());

        BigDecimal totalAmount = entries.stream()
                .map(AftEntryRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        control.setTotalAmount(totalAmount);

        StringBuilder aftFile = new StringBuilder();
        aftFile.append(header.toRecordString()).append("\n");

        for (AftEntryRecord entry : entries) {
            aftFile.append(entry.toRecordString()).append("\n");
        }

        aftFile.append(control.toRecordString());

        return aftFile.toString();
    }
}
