package com.loc.account_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountCreationRequest {
    private String id;
    private String email;
    private String currency;
    private double balance;
    private double reserved;
}
