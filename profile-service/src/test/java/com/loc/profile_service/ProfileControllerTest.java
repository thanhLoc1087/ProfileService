package com.loc.profile_service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.loc.common_service.utils.Constants;
import com.loc.profile_service.controller.ProfileController;
import com.loc.profile_service.dto.request.ProfileRequest;
import com.loc.profile_service.dto.response.ProfileResponse;
import com.loc.profile_service.service.ProfileService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
public class ProfileControllerTest {
    @Mock
    private ProfileService profileService;

    @InjectMocks
    private ProfileController profileController;

    private ProfileResponse profileResponse;

    private Gson gson = new Gson();

    @BeforeEach
    public void setUp() {
        profileResponse = new ProfileResponse(1, "dev@gmail.com", Constants.STATUS_PROFILE_ACTIVE, 10000, "Le Thanh Loc", "DEVELOPER");
        ReflectionTestUtils.setField(profileController, "profileService", profileService);
    }

    @Test
    void getAllProfileSuccess() {
        when(profileService.getAllProfiles()).thenReturn(Flux.just(profileResponse));
        profileController.getAllProfiles()
            .getBody()
            .doOnNext(response -> 
                Assertions.assertEquals(profileResponse, response)
            ).subscribe();
    }

    @Test
    void checkDuplicate() {
        when(profileService.checkDuplicate("test")).thenReturn(Mono.just(true));
        profileController.checkDuplicate(anyString())
            .getBody()
            .doOnNext(aBoolean -> Assertions.assertEquals(aBoolean, true)).subscribe();
    }

    @Test
    void createNewProfile() throws JsonProcessingException {
        when(profileService.createNewProfile(any(ProfileRequest.class)))
            .thenReturn(Mono.just(profileResponse));
        
        profileController.createProfile(gson.toJson(profileResponse))
            .getBody()
            .doOnNext(response -> Assertions.assertEquals(profileResponse, response));
    }

}
