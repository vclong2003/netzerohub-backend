package com.netzerohub.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netzerohub.auth.dto.TokenPairDto;
import com.netzerohub.auth.entity.Account;
import com.netzerohub.auth.form.LoginForm;
import com.netzerohub.auth.form.RegisterForm;
import com.netzerohub.auth.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("")
    public ResponseEntity<List<Account>> testGetAllAccounts() {
        return ResponseEntity.ok(authService.getAllAccounts());
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterForm form) {
        authService.register(form);
        return ResponseEntity.ok().build();
    }

    @PostMapping("login")
    public ResponseEntity<TokenPairDto> login(@RequestBody @Valid LoginForm form) {
        TokenPairDto dto = authService.login(form);
        return ResponseEntity.ok(dto);
    }

}
