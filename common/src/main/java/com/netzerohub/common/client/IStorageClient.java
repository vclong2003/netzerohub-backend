package com.netzerohub.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.netzerohub.common.config.FeignConfig;
import com.netzerohub.common.dto.BucketDto;
import com.netzerohub.common.dto.FileDto;

@FeignClient(name = "storage", path = "/api/storage", configuration = FeignConfig.class)
public interface IStorageClient {
    @GetMapping("/signed-url")
    public FileDto getSignedUrl(@RequestParam() String bucketUrl);

    @GetMapping("/bucket-url")
    public BucketDto getBucketUrl(@RequestParam() String signedUrl);
}
