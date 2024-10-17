package com.loc.account_service.event;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.loc.account_service.dto.request.AccountCreationRequest;
import com.loc.account_service.dto.response.ProfileResponse;
import com.loc.account_service.service.AccountService;
import com.loc.common_service.utils.Constants;

import lombok.extern.slf4j.Slf4j;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

@Service
@Slf4j
public class EventConsumer {
    Gson gson = new Gson();
    @Autowired
    AccountService accountService;
    @Autowired
    EventProducer eventProducer;
    
    public EventConsumer(ReceiverOptions<String, String> receiverOptions) {
        KafkaReceiver.create(receiverOptions.subscription(
            Collections.singleton(Constants.PROFILE_ONBOARDING_TOPIC))
        ).receive().subscribe(this::profileOnboarding);
    }

    public void profileOnboarding(ReceiverRecord<String, String> receiverRecord) {
        log.info("Profile onboarding event");
        ProfileResponse profileResponse = gson.fromJson(receiverRecord.value(), ProfileResponse.class);
        AccountCreationRequest accountCreationRequest = AccountCreationRequest.builder()
            .email(profileResponse.getEmail())
            .balance(profileResponse.getInitialBalance())
            .reserved(0)
            .currency("USD")
            .build();
        accountService.createNewAccount(accountCreationRequest)
            .subscribe(res -> {
                profileResponse.setStatus(Constants.STATUS_PROFILE_ACTIVE);
                eventProducer.send(Constants.PROFILE_ONBOARD_TOPIC, gson.toJson(profileResponse)).subscribe();
            });        
    }
}
