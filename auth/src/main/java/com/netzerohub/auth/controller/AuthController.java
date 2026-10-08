package com.netzerohub.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.netzerohub.auth.dto.TokenPairDto;
import com.netzerohub.auth.entity.Account;
import com.netzerohub.auth.form.ForgotPasswordForm;
import com.netzerohub.auth.form.LoginForm;
import com.netzerohub.auth.form.RegisterForm;
import com.netzerohub.auth.form.ResetPasswordForm;
import com.netzerohub.auth.form.VerifyAccountForm;
import com.netzerohub.auth.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("")
    public ResponseEntity<List<Account>> testGetAllAccounts() {
        return ResponseEntity.ok(authService.getAllAccounts());
    }

    @PostMapping("register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterForm form) {
        authService.register(form);
        return ResponseEntity.ok().build();
    }

    @PostMapping("verify")
    public ResponseEntity<Void> verifyByEmailOtp(@Valid @RequestBody VerifyAccountForm form) {
        authService.verifyAccount(form);
        return ResponseEntity.ok().build();
    }

    @PostMapping("login")
    public ResponseEntity<TokenPairDto> login(@RequestBody @Valid LoginForm form) {
        TokenPairDto dto = authService.login(form);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("refresh")
    public ResponseEntity<TokenPairDto> refreshAccessToken(@RequestParam String refreshToken) {
        TokenPairDto dto = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordForm form) {
        authService.forgotPassword(form);
        return ResponseEntity.ok().build();
    }

    @PostMapping("reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordForm form) {
        authService.resetPassword(form);
        return ResponseEntity.ok().build();
    }
}
