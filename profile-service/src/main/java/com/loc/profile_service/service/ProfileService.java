package com.loc.profile_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loc.profile_service.dto.response.ProfileResponse;
import com.loc.profile_service.mapper.ProfileMapper;
import com.loc.profile_service.repository.ProfileRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProfileService {
    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    ProfileMapper profileMapper;

    public Flux<ProfileResponse> getAllProfiles() {
        return profileRepository.findAll()
            .map(profileMapper::toProfileResponse)
            .switchIfEmpty(Mono.error(new Exception("Profile list empty")));
    }
}
