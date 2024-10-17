package com.loc.profile_service.event;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Service
@RequiredArgsConstructor
public class EventProducer {
    private final KafkaSender<String, String> sender;
    public Mono<String> send(String topic, String message) {
        return sender.send(Mono.just(
            SenderRecord.create(
                new ProducerRecord<String, String>(topic, message),
                message)))
            .then()
            .thenReturn("OK");
    }
}
