package com.learnkafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnkafka.domain.LibraryEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class LibraryEventsProducer {

    @Value("${spring.kafka.topic}")
    public String topic;


    private final KafkaTemplate<Integer, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    public LibraryEventsProducer(KafkaTemplate<Integer, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }


//    public CompletableFuture<SendResult<Integer, String>> sendLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException {
//        var key = libraryEvent.getLibraryEventId();
//        var value = objectMapper.writeValueAsString(libraryEvent);
//
//        //1. get metadata about the kafka cluster
//        //2. send message happens - returns a completable future
//        // asynchronous way of sending messages
//        var completableFuture = kafkaTemplate.send(topic, key, value);
//
//        //handling errors, if the completable future does not return successfully
//        return completableFuture.whenComplete((sendResult, throwable) -> {
//            if(throwable != null) {
//                handleFailure(key, value, throwable);
//            } else{
//                handleSuccess(key, value, sendResult);
//            }
//        });
//    }


//    public SendResult<Integer, String> sendLibraryEvent_approach2(LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException {
//        var key = libraryEvent.getLibraryEventId();
//        var value = objectMapper.writeValueAsString(libraryEvent);
//
//        //1. get metadata about the kafka cluster
//        //2. block and wait until the message is sent to the kafka cluster
//        // synchronous way of sending messages
//        var sendResult = kafkaTemplate.send(topic, key, value).get();
//        handleSuccess(key, value, sendResult);
//        return sendResult;
//    }



    public CompletableFuture<SendResult<Integer, String>> sendLibraryEvent_approach3(LibraryEvent libraryEvent) throws JsonProcessingException {
        var key = libraryEvent.libraryEventId();
        var value = objectMapper.writeValueAsString(libraryEvent);

        var producerRecord = buildProducerRecord(key, value);

        //1. get metadata about the kafka cluster
        //2. send message happens - returns a completable future
        var completableFuture = kafkaTemplate.send(producerRecord);

        //handling errors, if the completable future does not return successfully
        return completableFuture.whenComplete((sendResult, throwable) -> {
            if(throwable != null) {
                handleFailure(key, value, throwable);
            } else{
                handleSuccess(key, value, sendResult);
            }
        });
    }
//
//    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value) {
//        return new ProducerRecord<>(topic, key, value);
//    }


//Sending kafkaRecord with headers using kafkaTemplate
//Remove the above ProducerRecord and use the below method for sendLibraryEvent_approach3
    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value) {
        //var recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));
        List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));
        return new ProducerRecord<>(topic, null, key, value, recordHeaders);
    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> sendResult) {
        log.info("Message sent successfully for the key : {} and the value : {} and the partition is {}", key, value, sendResult.getRecordMetadata().partition());
    }

    private void handleFailure(Integer key, String value, Throwable throwable) {
        log.error("ERROR sending the message and the exception is {} ", throwable.getMessage(), throwable);
    }
}
