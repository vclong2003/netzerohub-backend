package com.netzerohub.auth.service;

import org.springframework.stereotype.Service;

import com.netzerohub.auth.entity.Account;
import com.netzerohub.auth.repository.IAccountRepository;
import com.netzerohub.common.enums.ErrorCode;
import com.netzerohub.common.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final IAccountRepository accountRepository;

    public Account getAccountById(Integer accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new BadRequestException(ErrorCode.AccountNotFound, "Account not found"));
    }
}
