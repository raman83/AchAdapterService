package com.batch.dto;

import java.util.List;


import lombok.Data;

@Data
public class AftBatchRequest {
    private AftFileHeaderDto header;
    private List<AftPaymentRequest> payments;
}
