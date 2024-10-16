package com.loc.account_service.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
@Builder
public class Account {
    @Id
    private String id;
    private String email;
    private String currency;
    private double balance;
    private double reserved;
}
