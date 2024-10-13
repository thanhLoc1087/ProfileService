package com.loc.profile_service.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.loc.profile_service.data.Profile;

public interface ProfileRepository extends ReactiveCrudRepository<Profile, Long>{
    
}
