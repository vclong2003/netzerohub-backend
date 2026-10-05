package com.netzerohub.common.dto;

import java.time.LocalDateTime;

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
}
