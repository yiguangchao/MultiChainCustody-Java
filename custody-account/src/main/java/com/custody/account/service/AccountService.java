package com.custody.account.service;

import com.custody.account.entity.Account;
import com.custody.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;

    public void updateBalance(String address, String chainName, BigDecimal delta) {
        Account account = repository.findById(address)
                .orElseGet(() -> {
                    Account newAcc = new Account();
                    newAcc.setAddress(address);
                    newAcc.setChainName(chainName);
                    newAcc.setBalance(BigDecimal.ZERO);
                    newAcc.setStatus("ACTIVE");
                    return newAcc;
                });

        account.setBalance(account.getBalance().add(delta));
        repository.save(account);
    }

    public BigDecimal getBalance(String address) {
        return repository.findById(address)
                .map(Account::getBalance)
                .orElse(BigDecimal.ZERO);
    }
}