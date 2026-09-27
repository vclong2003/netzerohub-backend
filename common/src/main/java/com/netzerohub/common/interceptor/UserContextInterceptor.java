package com.netzerohub.common.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.netzerohub.common.context.AccountContext;
import com.netzerohub.common.enums.AccountRole;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserContextInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accountId = request.getHeader("X-Account-Id");
        String accountRole = request.getHeader("X-Account-Role");

        if (accountId != null) {
            AccountContext context = AccountContext.builder()
                    .accountId(Integer.parseInt(accountId))
                    .accountRole(AccountRole.valueOf(accountRole))
                    .build();
            AccountContext.set(context);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        AccountContext.clear();
    }
}
