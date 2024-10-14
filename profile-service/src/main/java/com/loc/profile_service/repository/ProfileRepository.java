package com.loc.profile_service.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.loc.profile_service.data.Profile;

import reactor.core.publisher.Mono;

public interface ProfileRepository extends ReactiveCrudRepository<Profile, Long>{
    Mono<Profile> findByEmail(String email);
}
