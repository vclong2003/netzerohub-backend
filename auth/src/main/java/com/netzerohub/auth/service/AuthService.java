package com.netzerohub.auth.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.netzerohub.auth.dto.TokenPairDto;
import com.netzerohub.auth.entity.Account;
import com.netzerohub.auth.form.LoginForm;
import com.netzerohub.auth.form.RegisterForm;
import com.netzerohub.auth.repository.IAccountRepository;
import com.netzerohub.auth.util.PasswordUtils;
import com.netzerohub.common.enums.ErrorCode;
import com.netzerohub.common.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final IAccountRepository accountRepository;
    private final PasswordUtils passwordUtils;
    private final TokenService tokenService;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Void register(RegisterForm form) {
        // check if account exists
        if (accountRepository.existsByEmail(form.getEmail())) {
            throw new BadRequestException("Account already exists");
        }

        String hashedPassword = passwordUtils.hashPassword(form.getPassword());

        Account account = new Account();
        account.setEmail(form.getEmail());
        account.setPassword(hashedPassword);
        account.setFirstName(form.getFirstName());
        account.setLastName(form.getLastName());
        accountRepository.save(account);

        return null;
    }

    public TokenPairDto login(LoginForm form) {
        Account account = accountRepository.findOneByEmail(form.getEmail())
                .orElseThrow(() -> new BadRequestException(ErrorCode.AccountNotFound, "Invalid email or password"));

        if (!passwordUtils.verifyPassword(form.getPassword(), account.getPassword())) {
            throw new BadRequestException(ErrorCode.InvalidCredentials, "Invalid email or password");
        }

        if (!account.getIsVerified()) {
            throw new BadRequestException(ErrorCode.AccountNotVerified, "Account is not verified");
        }

        String refreshToken = tokenService.genAndSaveRefreshToken(account.getId(), account.getRole());
        String accessToken = tokenService.genAccessToken(account.getId(), account.getRole());

        return new TokenPairDto(refreshToken, accessToken);
    }

}
