package com.netzerohub.common.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.netzerohub.common.context.RequestContext;
import com.netzerohub.common.enums.AccountRole;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestContextInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accountId = request.getHeader("X-Account-Id");
        String accountRole = request.getHeader("X-Account-Role");
        String internalRequest = request.getHeader("X-Internal-Request");

        if (accountId != null) {
            RequestContext context = RequestContext.builder()
                    .accountId(Integer.parseInt(accountId))
                    .accountRole(AccountRole.valueOf(accountRole))
                    .isInternalRequest(Boolean.parseBoolean(internalRequest))
                    .build();
            RequestContext.set(context);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        RequestContext.clear();
    }
}
