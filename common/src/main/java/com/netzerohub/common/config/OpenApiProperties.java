package com.netzerohub.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "openapi")
public record OpenApiProperties(String defaultServerUrl) {
}
