package com.optimagrowth.license.events.sink;

import com.optimagrowth.license.events.model.OrganizationChangeModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;


/**
 * Sink == Consumer -> reads messages from Kafka topic
 * See <a href="https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html#spring-cloud-stream-preface-adding-message-handler">docs</a>
 * <p>
 * To get a message from a topic:
 *      docker exec -it docker-kafkaserver-1 /bin/bash
 *      kafka-topics --bootstrap-server kafkaserver:9092 --list
 *      kafka-console-consumer --bootstrap-server kafkaserver:9092 --topic organization-topic --from-beginning --max-messages 1
 */
@Configuration
public class OrganizationChangeSink {

    /**
     * @see config/licensing-service.yml#spring.cloud.stream.bindings
     * See <a href="https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html#_functional_binding_names">docs</a>
     */
    @Bean
    public Consumer<OrganizationChangeModel> sinkBinding() {
        return event -> {
            System.out.println("Received from Kafka topic: " + event);
        };
    }
}
