package com.optimagrowth.license.events.sink;

import com.optimagrowth.license.events.handler.OrganizationChangeEventHandler;
import com.optimagrowth.license.events.model.OrganizationChangeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
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

    private static final Logger logger = LoggerFactory.getLogger(OrganizationChangeSink.class);

    @Autowired
    OrganizationChangeEventHandler organizationChangeEventHandler;

    /**
     * @see config/licensing-service.yml#spring.cloud.stream.bindings
     * See <a href="https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html#_functional_binding_names">docs</a>
     * <p>
     * Why List<?>,
     * see: <a href="https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream-binder-kafka.html#_consuming_batches">docs</>
     */
    @Bean
    public Consumer<List<OrganizationChangeModel>> sinkBinding() {
        return events -> {
            logger.debug("Sinking Kafka records:");
            events.forEach(event -> organizationChangeEventHandler.processOrganizationChangedEvent(event));
        };
    }
}
