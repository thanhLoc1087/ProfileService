package com.loc.profile_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loc.profile_service.dto.request.ProfileRequest;
import com.loc.profile_service.dto.response.ProfileResponse;
import com.loc.profile_service.service.ProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/${api.prefix}/profiles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileController {
    ProfileService profileService;

    @GetMapping
    public ResponseEntity<Flux<ProfileResponse>> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    @GetMapping("/checkDuplicate/{email}")
    public ResponseEntity<Mono<Boolean>> checkDuplicate(@PathVariable String email) {
        return ResponseEntity.ok(profileService.checkDuplicate(email));
    }

    @PostMapping
    public ResponseEntity<Mono<ProfileResponse>> createProfile(@RequestBody ProfileRequest request) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(profileService.createNewProfile(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Mono<String>> deleteProfile(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.deleteProfile(id));
    }
} 
