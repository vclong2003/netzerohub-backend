package com.netzerohub.storage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws")
public record AwsProperties(S3 s3) {
    public record S3(String endpoint, String bucket, String accessKey, String secretKey) {
    }
}
