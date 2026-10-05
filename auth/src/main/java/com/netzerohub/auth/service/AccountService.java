package com.netzerohub.auth.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.netzerohub.auth.entity.Account;
import com.netzerohub.auth.form.UpdateAccountForm;
import com.netzerohub.auth.repository.IAccountRepository;
import com.netzerohub.common.enums.ErrorCode;
import com.netzerohub.common.exception.BadRequestException;
import com.netzerohub.common.util.ObjectUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final IAccountRepository accountRepository;

    public Account getAccountById(Integer accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new BadRequestException(ErrorCode.AccountNotFound, "Account not found"));
    }

    public Account getAccountByCode(String code) {
        return accountRepository.findOneByCode(code)
                .orElseThrow(() -> new BadRequestException(ErrorCode.AccountNotFound, "Account not found"));
    }

    public Account updateAccount(Integer accountId, UpdateAccountForm form) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new BadRequestException(ErrorCode.AccountNotFound, "Account not found"));

        BeanUtils.copyProperties(form, account, ObjectUtils.getNullPropertyNames(form));

        return accountRepository.save(account);
    }
}
