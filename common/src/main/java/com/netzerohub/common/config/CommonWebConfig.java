package com.netzerohub.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.netzerohub.common.interceptor.RoleAuthInterceptor;
import com.netzerohub.common.interceptor.UserContextInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@ComponentScan(basePackages = "com.netzerohub.common")
@ConditionalOnWebApplication
@RequiredArgsConstructor
public class CommonWebConfig implements WebMvcConfigurer {
    private final UserContextInterceptor userContextInterceptor;
    private final RoleAuthInterceptor roleAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userContextInterceptor); // add user to context
        registry.addInterceptor(roleAuthInterceptor); // filter based on role
    }
}
