package com.loc.profile_service.dto.request;

import com.loc.profile_service.enums.ProfileStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {
    private long id;
    private String email;
    private ProfileStatus status;
    private double initialBalance;
    private String name;
    private String role;
}
