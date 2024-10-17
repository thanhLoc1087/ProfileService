package com.loc.account_service.service;

import org.springframework.stereotype.Service;

import com.loc.account_service.dto.request.AccountCreationRequest;
import com.loc.account_service.dto.response.AccountResponse;
import com.loc.account_service.mapper.AccountMapper;
import com.loc.account_service.repository.AccountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public Mono<AccountResponse> createNewAccount(AccountCreationRequest request) {
        return Mono.just(request)
            .map(accountMapper::toAccount)
            .flatMap(accountRepository::save)
            .map(accountMapper::toAccountResponse)
            .doOnError(throwable -> log.error(throwable.getMessage(), throwable));
    }
}
