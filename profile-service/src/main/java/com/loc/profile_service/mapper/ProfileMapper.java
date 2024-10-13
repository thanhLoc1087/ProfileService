package com.loc.profile_service.mapper;

import org.mapstruct.Mapper;

import com.loc.profile_service.data.Profile;
import com.loc.profile_service.dto.request.ProfileRequest;
import com.loc.profile_service.dto.response.ProfileResponse;

@Mapper(componentModel="spring")
public interface ProfileMapper {
    public Profile toProfile(ProfileRequest request);
    public ProfileResponse toProfileResponse(Profile profile);
}
