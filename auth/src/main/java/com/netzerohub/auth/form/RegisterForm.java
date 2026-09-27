package com.netzerohub.auth.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class RegisterForm {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Min(8)
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}
