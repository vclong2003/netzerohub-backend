package com.netzerohub.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netzerohub.auth.dto.AccountDto;
import com.netzerohub.auth.entity.Account;
import com.netzerohub.auth.form.UpdateAccountForm;
import com.netzerohub.auth.service.AccountService;
import com.netzerohub.common.context.RequestContext;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequestMapping("/api/account")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("me")
    public ResponseEntity<AccountDto> getCurrentAccount() {
        RequestContext ctx = RequestContext.get();
        Account account = accountService.getAccountById(ctx.getAccountId());
        return ResponseEntity.ok(new AccountDto(account));
    }

    @PostMapping("me")
    public ResponseEntity<AccountDto> updateCurrentAccount(@Valid @RequestBody UpdateAccountForm form) {
        RequestContext ctx = RequestContext.get();
        Account account = accountService.updateAccount(ctx.getAccountId(), form);
        return ResponseEntity.ok(new AccountDto(account));
    }

    @GetMapping("{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable("id") Integer id) {
        Account account = accountService.getAccountById(id);
        return ResponseEntity.ok(new AccountDto(account));
    }

    @GetMapping("code/{code}")
    public ResponseEntity<AccountDto> getAccountByCode(@PathVariable("code") String code) {
        Account account = accountService.getAccountByCode(code);
        return ResponseEntity.ok(new AccountDto(account));
    }

}
