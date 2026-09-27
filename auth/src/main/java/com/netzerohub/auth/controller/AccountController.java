package com.netzerohub.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netzerohub.auth.dto.AccountDto;
import com.netzerohub.auth.entity.Account;
import com.netzerohub.auth.service.AccountService;
import com.netzerohub.common.context.AccountContext;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/account")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("me")
    public ResponseEntity<AccountDto> getCurrentAccount() {
        AccountContext ctx = AccountContext.get();

        Account account = accountService.getAccountById(ctx.getAccountId());

        return ResponseEntity.ok(new AccountDto(account));
    }
}
