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
    protected Integer id;
    protected String code;
    protected String email;
    protected AccountRole role;
    protected String phone;
    protected String firstName;
    protected String lastName;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
}
