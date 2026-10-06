package com.netzerohub.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.netzerohub.common.client.IAuthClient;
import com.netzerohub.common.client.IStorageClient;

import feign.RequestInterceptor;

@Configuration
@ComponentScan(basePackages = "com.netzerohub.common")
@ConditionalOnClass(RequestInterceptor.class)
@EnableFeignClients(clients = { IAuthClient.class, IStorageClient.class })
public class FeignAutoConfig {
}
