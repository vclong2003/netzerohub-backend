package com.netzerohub.storage.controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.netzerohub.storage.dto.BucketDto;
import com.netzerohub.storage.dto.FileDto;
import com.netzerohub.storage.service.StorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
public class StorageController {
    private final StorageService storageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BucketDto> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String bucketUrl = storageService.uploadFile(file);
        return ResponseEntity.ok(new BucketDto(bucketUrl));
    }

    @GetMapping("/signed-url")
    public ResponseEntity<FileDto> getSignedUrl(@RequestParam("bucketUrl") String bucketUrl) {
        String signedUrl = storageService.generateSignedUrl(bucketUrl);
        return ResponseEntity.ok(new FileDto(signedUrl));
    }

    @GetMapping("/bucket-url")
    public ResponseEntity<BucketDto> getBucketUrl(@RequestParam("signedUrl") String signedUrl) {
        String bucketUrl = storageService.getBucketPathFromSignedUrl(signedUrl);
        return ResponseEntity.ok(new BucketDto(bucketUrl));
    }
}
