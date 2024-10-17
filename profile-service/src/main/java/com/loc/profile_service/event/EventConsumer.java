package com.loc.profile_service.event;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.loc.common_service.utils.Constants;
import com.loc.profile_service.dto.request.ProfileStatusRequest;
import com.loc.profile_service.dto.response.ProfileResponse;
import com.loc.profile_service.service.ProfileService;

import lombok.extern.slf4j.Slf4j;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

@Service
@Slf4j
public class EventConsumer {
    Gson gson = new Gson();

    @Autowired
    ProfileService profileService;
    
    public EventConsumer(ReceiverOptions<String, String> receiverOptions) {
        KafkaReceiver.create(receiverOptions.subscription(
                Collections.singleton(Constants.PROFILE_ONBOARD_TOPIC))
            ).receive().subscribe(this::profileOnboard);
    }

    public void profileOnboard(ReceiverRecord<String, String> receiverRecord) {
        log.info("Profile onboard successfully!");
        ProfileResponse profileResponse = gson.fromJson(receiverRecord.value(), ProfileResponse.class);

        profileService.updateProfileStatus(new ProfileStatusRequest(
            profileResponse.getEmail(),
            profileResponse.getStatus()
        )).subscribe();
    }
}
