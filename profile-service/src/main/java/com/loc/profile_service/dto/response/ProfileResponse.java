package com.loc.profile_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    private long id;
    private String email;
    private String status;
    private double initialBalance;
    private String name;
    private String role;
}
