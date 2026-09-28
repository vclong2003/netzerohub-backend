package com.netzerohub.apigateway.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security")
public record SecurityProperties(Jwt jwt, Exclude exclude) {
    public record Jwt(String publicKey) {
    }

    public record Exclude(List<String> routes) {
    }
}
