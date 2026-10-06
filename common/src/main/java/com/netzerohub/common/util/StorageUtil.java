package com.netzerohub.common.util;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import com.netzerohub.common.client.IStorageClient;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnClass(RequestInterceptor.class)
public class StorageUtil {
    private final IStorageClient storageClient;

    public String getBucketUrlFromSignedUrl(String signedUrl) {
        try {
            return storageClient.getBucketUrl(signedUrl).getBucketUrl();
        } catch (Exception e) {
            log.error("Error while getting bucket URL from signed URL: {}", e.getMessage());
            return "";
        }
    }

    public List<String> getBucketUrlsFromSignedUrls(List<String> signedUrls) {
        return signedUrls.stream()
                .map(this::getBucketUrlFromSignedUrl)
                .filter(bucketUrl -> !bucketUrl.isEmpty())
                .toList();
    }

    public String getSignedUrlFromBucketUrl(String bucketUrl) {
        try {
            return storageClient.getSignedUrl(bucketUrl).getSignedUrl();
        } catch (Exception e) {
            log.error("Error while getting signed URL from bucket URL: {}", e.getMessage());
            return "";
        }
    }

    public List<String> getSignedUrlsFromBucketUrls(List<String> bucketUrls) {
        return bucketUrls.stream()
                .map(this::getSignedUrlFromBucketUrl)
                .filter(signedUrl -> !signedUrl.isEmpty())
                .toList();
    }
}
