package com.netzerohub.auth.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.netzerohub.auth.entity.Account;
import com.netzerohub.auth.repository.IAccountRepository;
import com.netzerohub.auth.util.PasswordUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final IAccountRepository accountRepository;
    private final PasswordUtils passwordUtils;

    public void createAccount(String email, String password, String firstName, String lastName) {
        String hashedPassword = passwordUtils.hashPassword(password);

        Account account = new Account();
        account.setEmail(email);
        account.setPassword(hashedPassword);
        account.setFirstName(firstName);
        account.setLastName(lastName);
        accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}
