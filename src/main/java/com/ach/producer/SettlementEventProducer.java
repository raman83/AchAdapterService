package com.ach.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.batch.dto.AftSettlementTriggerEvent;
import com.batch.dto.BillSettlementTriggerEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SettlementEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    


    @Value("${kafka.topic.settlement}")
    private String topic;

    
    @Value("${kafka.billtopic.settlement}")
    private String billtopic;
    
    
    public void publish(AftSettlementTriggerEvent event) {
        kafkaTemplate.send(topic, event.getFileName(), event);
    }
    
    public void publishBill(BillSettlementTriggerEvent event) {
    	kafkaTemplate.send(billtopic, event.getFileName(), event);
    }
}
