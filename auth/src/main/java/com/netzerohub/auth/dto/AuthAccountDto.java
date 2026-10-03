package com.netzerohub.auth.dto;

import com.netzerohub.auth.entity.Account;
import com.netzerohub.common.dto.AccountDto;

public class AuthAccountDto extends AccountDto {
    public AuthAccountDto(Account entity) {
        this.id = entity.getId();
        this.code = entity.getCode();
        this.email = entity.getEmail();
        this.role = entity.getRole();
        this.phone = entity.getPhone();
        this.firstName = entity.getFirstName();
        this.lastName = entity.getLastName();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
