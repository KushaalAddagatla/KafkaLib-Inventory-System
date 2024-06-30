package com.learnkafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learnkafka.service.LibraryEventsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class LibraryEventsRetryConsumer {

    @Autowired
    LibraryEventsService libraryEventsService;

//    @KafkaListener(topics = "${topics.retry}", groupId = "retry-listener-group")
//    public void onMessage(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
//        log.info("ConsumerRecord in retry consumer: {} ", consumerRecord);
//        consumerRecord.headers()
//                        .forEach(header -> {
//                            log.info("key : {}, value : {} " , header.key(), new String(header.value()));
//                        });
//        libraryEventsService.processLibraryEvent(consumerRecord);
//    }


    @KafkaListener(topics = "${topics.retry}", autoStartup = "${retryListener.startup:false}",groupId = "retry-listener-group")
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord) {
        try {
            log.info("Received message: {}", consumerRecord.value());
            log.info("Headers:");
            consumerRecord.headers().forEach(header -> {
                log.info("  key: {}, value: {}", header.key(), new String(header.value()));
            });

            libraryEventsService.processLibraryEvent(consumerRecord);
            log.info("Processed message successfully");
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage());
            // Optionally handle or rethrow the exception depending on your recovery strategy
            // throw new RuntimeException("Failed to process message", e); // Uncomment if rethrowing
        }
    }
}
