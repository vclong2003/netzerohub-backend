package com.netzerohub.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.netzerohub.common.config.FeignConfig;
import com.netzerohub.common.dto.AccountDto;

@FeignClient(name = "auth", path = "/api/account", configuration = FeignConfig.class)
public interface IAuthClient {
    @GetMapping("{id}")
    AccountDto getAccountById(@PathVariable Integer id);

    @GetMapping("code/{code}")
    AccountDto getAccountByCode(@PathVariable String code);
}
