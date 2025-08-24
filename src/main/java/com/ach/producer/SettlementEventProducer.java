// SettlementEventProducer.java
package com.ach.producer;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.batch.dto.AftSettlementTriggerEvent;
import com.batch.dto.BillSettlementTriggerEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettlementEventProducer {

    @Qualifier("objectKafkaTemplate")
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.settlement}")
    private String topic;

    @Value("${kafka.billtopic.settlement}")
    private String billtopic;

    public void publish(AftSettlementTriggerEvent event) {
        String key = event.getFileName();
        kafkaTemplate.send(topic, key, event).whenComplete((res, ex) -> {
            if (ex != null) {
                log.error("[ACH] AFT publish FAILED topic={} key={}", topic, key, ex);
                return;
            }
            RecordMetadata m = res.getRecordMetadata();
            log.info("[ACH] AFT publish OK topic={} partition={} offset={} key={}",
                    m.topic(), m.partition(), m.offset(), key);
        });
        kafkaTemplate.flush();
    }

    public void publishBill(BillSettlementTriggerEvent event) {
        String key = event.getFileName();
        kafkaTemplate.send(billtopic, key, event).whenComplete((res, ex) -> {
            if (ex != null) {
                log.error("[ACH] BILL publish FAILED topic={} key={}", billtopic, key, ex);
                return;
            }
            RecordMetadata m = res.getRecordMetadata();
            log.info("[ACH] BILL publish OK topic={} partition={} offset={} key={}",
                    m.topic(), m.partition(), m.offset(), key);
        });
        kafkaTemplate.flush();
    }
}
