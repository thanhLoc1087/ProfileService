package com.loc.profile_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loc.profile_service.dto.response.ProfileResponse;
import com.loc.profile_service.service.ProfileService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/${api.prefix}/profiles")
public class ProfileController {
    @Autowired
    ProfileService profileService;

    @GetMapping
    public ResponseEntity<Flux<ProfileResponse>> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }
}
