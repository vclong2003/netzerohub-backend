package com.netzerohub.auth.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountForm {
    private String firstName;

    private String lastName;

    private String avatarUrl;

    private String phone;
}
