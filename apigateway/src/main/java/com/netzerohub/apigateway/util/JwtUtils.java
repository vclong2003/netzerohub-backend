package com.netzerohub.apigateway.util;

import org.springframework.stereotype.Component;

import com.netzerohub.apigateway.config.SecurityProperties;
import com.netzerohub.apigateway.dto.TokenPayloadDto;
import com.netzerohub.common.enums.AccountRole;

import lombok.RequiredArgsConstructor;

import java.security.PublicKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    private final SecurityProperties securityProperties;

    public TokenPayloadDto extractTokenPayload(String token) throws Exception {
        PublicKey publicKey = KeyUtils.parsePublicKey(securityProperties.jwt().publicKey());

        Claims claims = Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String accountId = String.valueOf(claims.get("accountId"));
        String roleStr = claims.get("role", String.class);
        AccountRole role = AccountRole.valueOf(roleStr);

        return new TokenPayloadDto(accountId, role);

    }
}
