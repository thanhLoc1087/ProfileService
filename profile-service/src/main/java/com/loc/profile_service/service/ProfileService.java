package com.loc.profile_service.service;

import org.springframework.stereotype.Service;

import com.loc.profile_service.dto.request.ProfileRequest;
import com.loc.profile_service.dto.response.ProfileResponse;
import com.loc.profile_service.enums.ProfileStatus;
import com.loc.profile_service.mapper.ProfileMapper;
import com.loc.profile_service.repository.ProfileRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileService {
    ProfileRepository profileRepository;
    ProfileMapper profileMapper;

    public Flux<ProfileResponse> getAllProfiles() {
        return profileRepository.findAll()
            .map(profileMapper::toProfileResponse)
            .switchIfEmpty(Mono.error(new Exception("Profile list empty")));
    }

    public Mono<Boolean> checkDuplicate(String email) {
        return profileRepository.findByEmail(email)
            .flatMap(profile -> Mono.just(true))
            .switchIfEmpty(Mono.just(false));
    }

    public Mono<ProfileResponse> createNewProfile(ProfileRequest request) {
        return checkDuplicate(request.getEmail())
            .flatMap(result -> {
                if (Boolean.TRUE.equals(result)) {
                    return Mono.error(new Exception("Duplicate Profile"));
                } else {
                    request.setStatus(ProfileStatus.PENDING);
                    return createProfile(request);
                }
            });
    }

    public Mono<ProfileResponse> createProfile(ProfileRequest request) {
        return Mono.just(request)
            .map(profileMapper::toProfile)
            .flatMap(profileRepository::save)
            .map(profileMapper::toProfileResponse)
            .doOnError(throwable -> log.error(throwable.getMessage(), throwable))
            .doOnSuccess(response -> {

            });
    }

    public Mono<String> deleteProfile(Long id) {
        return profileRepository.findById(id)
                .map(profile -> {
                    ProfileResponse deletedProfile = profileMapper.toProfileResponse(profile);
                    profileRepository.deleteById(id);
                    return "Profile: " + deletedProfile.getName() + " deleted!";
                })
                .defaultIfEmpty("Profile id: " + id + " not found");
   }
}
