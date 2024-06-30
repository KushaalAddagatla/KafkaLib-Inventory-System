package com.learnkafka.config;

import com.learnkafka.service.FailureService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.ContainerCustomizer;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.util.backoff.ExponentialBackOff;
import org.springframework.util.backoff.FixedBackOff;

import java.util.List;
import java.util.Objects;

@Configuration
@EnableKafka
@Slf4j
public class LibraryEventsConsumerConfig {

    public static final String RETRY = "RETRY";
    public static final String DEAD = "DEAD";
    public static final String SUCCESS = "SUCCESS";


    @Autowired
    KafkaTemplate kafkaTemplate;

    @Autowired
    FailureService failureService;

    @Value("${topics.retry}")
    private String retryTopic;

    @Value("${topics.dlt}")
    private String deadLetterTopic;

    public DeadLetterPublishingRecoverer publishingRecoverer() {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
                (r, e) -> {
            log.error("Exception in publishingRecoverer : {}", e.getMessage(), e);
            if(e.getCause() instanceof RecoverableDataAccessException) {
                return new TopicPartition(retryTopic, r.partition());
            }
            else {
                return new TopicPartition(deadLetterTopic, r.partition());
            }
                });
        return recoverer;
    }


    ConsumerRecordRecoverer consumerRecordRecoverer = (consumerRecord, e) -> {
        log.error("Exception in consumerRecordRecoverer : {}", e.getMessage(), e);
        var record = (ConsumerRecord<Integer, String>)consumerRecord;
        if(e.getCause() instanceof RecoverableDataAccessException) {
            //recovery logic
            log.info("Inside Recovery");
            failureService.saveFailedRecord(record, e, RETRY);
        }
        else {
            //non-recovery logic
            log.info("Inside Non-Recovery");

            failureService.saveFailedRecord(record, e, DEAD);

        }
    };


    public DefaultErrorHandler errorHandler() {
        var exceptionsToIgnoreList = List.of(
                IllegalArgumentException.class
        );

        var fixedBackoff = new FixedBackOff(1000L, 2);
        var expBackoff = new ExponentialBackOffWithMaxRetries(2);
        expBackoff.setInitialInterval(1000L);
        expBackoff.setMultiplier(2.0);
        expBackoff.setMaxInterval(2000L);

//        var errorHandler = new DefaultErrorHandler(fixedBackoff);
//        var errorHandler = new DefaultErrorHandler(expBackoff);
        var defaultErrorHandler = new DefaultErrorHandler(
//                publishingRecoverer(),
                consumerRecordRecoverer,
                expBackoff);


        defaultErrorHandler.setRetryListeners((record, ex, deliveryAttempt) -> {
            log.info("Failed Record in Retry Listener, Exception : {} , deliveryAttempt : {}", ex.getMessage(), deliveryAttempt);
        });

        defaultErrorHandler.addNotRetryableExceptions();
        exceptionsToIgnoreList.forEach(defaultErrorHandler::addNotRetryableExceptions);

        return defaultErrorHandler;
    }

    private KafkaProperties properties;

    @Bean
    @ConditionalOnMissingBean(
            name = {"kafkaListenerContainerFactory"}
    )
    ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(ConcurrentKafkaListenerContainerFactoryConfigurer configurer, ObjectProvider<ConsumerFactory<Object, Object>> kafkaConsumerFactory, ObjectProvider<ContainerCustomizer<Object, Object, ConcurrentMessageListenerContainer<Object, Object>>> kafkaContainerCustomizer, ObjectProvider<SslBundles> sslBundles) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory();
        configurer.configure(factory, (ConsumerFactory)kafkaConsumerFactory.getIfAvailable(() -> {
            return new DefaultKafkaConsumerFactory(this.properties.buildConsumerProperties((SslBundles)sslBundles.getIfAvailable()));
        }));

        Objects.requireNonNull(factory);
        kafkaContainerCustomizer.ifAvailable(factory::setContainerCustomizer);
        factory.setConcurrency(3);
        factory.setCommonErrorHandler(errorHandler());

        //factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }
}
