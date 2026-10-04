package com.netzerohub.auth.dto;

import java.time.LocalDateTime;

import com.netzerohub.auth.entity.Account;
import com.netzerohub.common.enums.AccountRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private Integer id;
    private String code;
    private String email;
    private AccountRole role;
    private String phone;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AccountDto(Account entity) {
        this.id = entity.getId();
        this.code = entity.getCode();
        this.email = entity.getEmail();
        this.role = entity.getRole();
        this.phone = entity.getPhone();
        this.firstName = entity.getFirstName();
        this.lastName = entity.getLastName();
        this.avatarUrl = entity.getAvatarUrl();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
