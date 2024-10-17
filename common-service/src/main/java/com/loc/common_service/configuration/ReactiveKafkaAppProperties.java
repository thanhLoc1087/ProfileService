package com.loc.common_service.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReactiveKafkaAppProperties {
    @Value("${kafka.bootstrap.servers}")
    public String bootstrapServer;

    @Value("${payment.kafka.consumer-group-id}")
    public String consumerGroupId;
}
