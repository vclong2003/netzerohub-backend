package com.netzerohub.auth.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netzerohub.auth.service.AuthService;

@Configuration
public class DataInitializerConfig {
    @Bean
    public CommandLineRunner initAdminAccount(AuthService authService) {
        return args -> authService.createAdminIfNotExist();
    }
}
