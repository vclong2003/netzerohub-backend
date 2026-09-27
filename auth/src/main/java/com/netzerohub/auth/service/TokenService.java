package com.netzerohub.auth.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.netzerohub.auth.entity.AccountToken;
import com.netzerohub.auth.repository.IAccountTokenRepository;
import com.netzerohub.auth.util.JwtUtils;
import com.netzerohub.common.enums.EAccountRole;
import com.netzerohub.common.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final IAccountTokenRepository tokenRepository;
    private final JwtUtils jwtUtils;

    public String genAndSaveRefreshToken(Integer accountId, EAccountRole accountRole) {
        AccountToken newAccountToken = new AccountToken();

        String token = jwtUtils.generateRefreshToken();

        newAccountToken.setAccountId(accountId);
        newAccountToken.setAccountRole(accountRole);
        newAccountToken.setToken(token);
        newAccountToken.setCreatedAt(LocalDateTime.now());

        tokenRepository.save(newAccountToken);

        return token;
    }

    public String genAccessToken(Integer accountId, EAccountRole accountRole) {
        Map<String, Object> tokenPayload = new HashMap<>();
        tokenPayload.put("accountId", accountId);
        tokenPayload.put("role", accountRole);

        try {
            return jwtUtils.generateAccessToken(accountId.toString(), tokenPayload);
        } catch (Exception e) {
            throw new BadRequestException("Failed to generate access token");
        }
    }

    public void revokeRefreshToken(String refreshToken) {
        tokenRepository.deleteById(refreshToken);
    }

    public String refreshAccessToken(String refreshToken) {
        AccountToken accountToken = tokenRepository.findById(refreshToken)
                .orElseThrow(() -> new BadRequestException("Invalid or expired refresh token"));
        return genAccessToken(accountToken.getAccountId(), accountToken.getAccountRole());
    }
}
