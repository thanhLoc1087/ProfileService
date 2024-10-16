package com.loc.account_service.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.loc.account_service.data.Account;

public interface AccountRepository extends ReactiveCrudRepository<Account, String>{
    
}
