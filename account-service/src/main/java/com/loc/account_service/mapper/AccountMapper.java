package com.loc.account_service.mapper;

import org.mapstruct.Mapper;

import com.loc.account_service.data.Account;
import com.loc.account_service.dto.request.AccountCreationRequest;
import com.loc.account_service.dto.response.AccountResponse;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    public Account toAccount(AccountCreationRequest request);
    public AccountResponse toAccountResponse(Account account);
}
