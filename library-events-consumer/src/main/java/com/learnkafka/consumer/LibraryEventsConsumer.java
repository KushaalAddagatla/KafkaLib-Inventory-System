package com.learnkafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learnkafka.service.LibraryEventsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component  //comment this line for manual acknowledgement to __consumerOffset
@Slf4j
public class LibraryEventsConsumer {

    @Autowired
    LibraryEventsService libraryEventsService;

    @KafkaListener(topics = {"library-events"}, groupId = "library-events-listener-group")
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        log.info("ConsumerRecord : {} ", consumerRecord);
        libraryEventsService.processLibraryEvent(consumerRecord);
    }
}



//@Component
//@Slf4j
//public class LibraryEventsConsumer {
//
//    @Autowired
//    private LibraryEventsService libraryEventsService;
//
//    @KafkaListener(topics = "library-events", groupId = "library-events-listener-group")
//    public void onMessage(ConsumerRecord<Integer, String> consumerRecord) {
//        try {
//            log.info("Received message: {}", consumerRecord.value());
//            libraryEventsService.processLibraryEvent(consumerRecord);
//            log.info("Processed message successfully");
//        } catch (RecoverableDataAccessException e) {
//            // Handle recoverable exception, optionally log and retry
//            log.error("Recoverable exception occurred: {}", e.getMessage());
//            // Optionally retry logic, e.g., using Spring Retry or Kafka's built-in retry mechanisms
//        } catch (Exception e) {
//            log.error("Exception occurred while processing message: {}", e.getMessage());
//            // Handle other exceptions as needed
//        }
//    }
//}

