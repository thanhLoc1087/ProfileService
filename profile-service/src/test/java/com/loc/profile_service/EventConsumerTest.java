package com.loc.profile_service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.gson.Gson;
import com.loc.common_service.utils.Constants;
import com.loc.profile_service.dto.request.ProfileStatusRequest;
import com.loc.profile_service.dto.response.ProfileResponse;
import com.loc.profile_service.event.EventConsumer;
import com.loc.profile_service.service.ProfileService;

import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

@ExtendWith(SpringExtension.class)
public class EventConsumerTest {
    @InjectMocks
    private EventConsumer eventConsumer;

    @Mock
    private ProfileService profileService;

    @Mock
    private ReceiverOptions<String, String> receiverOptions;

    @Mock
    private ReceiverRecord<String, String> receiverRecord;

    private ProfileResponse profileResponse;

    private Gson gson = new Gson();

    @BeforeEach
    void setUp() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "sample-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        receiverOptions = ReceiverOptions.create(props);
        profileResponse = new ProfileResponse(1, "dev@gmail.com", Constants.STATUS_PROFILE_ACTIVE, 10000, "Le Thanh Loc", "DEVELOPER");
        ReflectionTestUtils.setField(eventConsumer, "profileService", profileService);
    }

    @Test
    void profileOnboard() {
        when(receiverRecord.value()).thenReturn(gson.toJson(profileResponse));
        when(profileService.updateProfileStatus(any(ProfileStatusRequest.class)))
            .thenReturn(Mono.just(profileResponse));
        
        eventConsumer.profileOnboard(receiverRecord);
        verify(profileService, times(1)).updateProfileStatus(any(ProfileStatusRequest.class));
    }
}
