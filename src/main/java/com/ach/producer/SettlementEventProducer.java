package com.ach.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ach.dto.AftSettlementTriggerEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SettlementEventProducer {

    private final KafkaTemplate<String, AftSettlementTriggerEvent> kafkaTemplate;

    @Value("${kafka.topic.settlement}")
    private String topic;

    public void publish(AftSettlementTriggerEvent event) {
        kafkaTemplate.send(topic, event.getFileName(), event);
    }
}
