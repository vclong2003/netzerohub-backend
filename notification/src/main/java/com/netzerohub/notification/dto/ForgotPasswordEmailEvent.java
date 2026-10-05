package com.netzerohub.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordEmailEvent {
    private String email;
    private String otp;
    private int ttlMinutes;
}
