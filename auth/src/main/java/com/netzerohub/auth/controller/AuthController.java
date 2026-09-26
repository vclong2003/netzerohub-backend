package com.netzerohub.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netzerohub.auth.entity.Account;
import com.netzerohub.auth.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

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

    @PostMapping("")
    public ResponseEntity<Void> testCreateAccount(@RequestBody Map<String, Object> body) {
        String email = (String) body.get("email");
        String password = (String) body.get("password");
        String firstName = (String) body.get("firstName");
        String lastName = (String) body.get("lastName");

        authService.createAccount(email, password, firstName, lastName);
        return ResponseEntity.ok().build();
    }

}
