package com.loc.profile_service.service;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.loc.common_service.common.CommonException;
import com.loc.common_service.utils.Constants;
import com.loc.profile_service.dto.request.ProfileRequest;
import com.loc.profile_service.dto.request.ProfileStatusRequest;
import com.loc.profile_service.dto.response.ProfileResponse;
import com.loc.profile_service.event.EventProducer;
import com.loc.profile_service.mapper.ProfileMapper;
import com.loc.profile_service.repository.ProfileRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
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
    EventProducer eventProducer;

    @NonFinal
    Gson gson = new Gson();

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
                    return Mono.error(new CommonException("PF02","Duplicate profile !", HttpStatus.BAD_REQUEST));
                } else {
                    request.setStatus(Constants.STATUS_PROFILE_PENDING);
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
                if (Objects.equals(response.getStatus(), Constants.STATUS_PROFILE_PENDING)) {
                    response.setInitialBalance(request.getInitialBalance());
                    eventProducer.send(Constants.PROFILE_ONBOARDING_TOPIC, gson.toJson(request)).subscribe();
                }
            });
    }

    public Mono<ProfileResponse> updateProfileStatus(ProfileStatusRequest request) {
        return getProfileByEmail(request.getEmail())
            .map(profileMapper::toProfile)
            .flatMap(profile -> {
                profile.setStatus(request.getStatus());
                return profileRepository.save(profile);
            })
            .map(profileMapper::toProfileResponse)
            .doOnError(throwable -> log.error(throwable.getMessage(), throwable));
    }

    public Mono<ProfileResponse> getProfileByEmail(String email) {
        return profileRepository.findByEmail(email)
            .map(profileMapper::toProfileResponse)
            .switchIfEmpty(Mono.error(new CommonException("PF03", "Profile not found.", HttpStatus.BAD_REQUEST)));
    }

    public Mono<String> deleteProfile(Long id) {
        return profileRepository.findById(id)
            .map(profile -> {
                ProfileResponse deletedProfile = profileMapper.toProfileResponse(profile);
                profileRepository.deleteById(id).subscribe();
                return "Profile: " + deletedProfile.getName() + " deleted!";
            })
            .defaultIfEmpty("Profile id: " + id + " not found");
   }
}
