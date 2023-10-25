package com.optimagrowth.organization.events.producer;

import com.optimagrowth.organization.events.model.ActionEnum;
import com.optimagrowth.organization.events.model.OrganizationChangeModel;
import com.optimagrowth.organization.utils.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;


/**
 * Source == Producer == Supplier -> writes messages to Kafka topic
 * See <a href="https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html#_sending_arbitrary_data_to_an_output_e_g_foreign_event_driven_sources">docs</a>
 * <p>
 * To get a message from a topic:
 *      docker exec -it docker-kafkaserver-1 /bin/bash
 *      kafka-topics --bootstrap-server kafkaserver:9092 --list
 *      kafka-console-consumer --bootstrap-server kafkaserver:9092 --topic organization-topic --from-beginning --max-messages 1
 */
@Component
public class OrganizationChangeSource {

    /**
     * @see config/organization-service.yml#spring.cloud.stream.bindings
     */
    static final public String KAFKA_BINDING_ORGANIZATION_SERVICE = "producerBinding-out-0";

    @Autowired
    private StreamBridge streamBridge;

    public void delegateOrganizationChangeEventSupplier(String organizationId, ActionEnum action) {
        String eventOrganizationId = organizationId == null || organizationId.isBlank() ?
                UserContextHolder.getContext().getOrganizationId() :
                organizationId;
        String correlationId = UserContextHolder.getContext().getCorrelationId();

        OrganizationChangeModel event = new OrganizationChangeModel(
                action.getValue(),
                eventOrganizationId,
                correlationId);

        streamBridge.send(KAFKA_BINDING_ORGANIZATION_SERVICE, event);
    }
}
