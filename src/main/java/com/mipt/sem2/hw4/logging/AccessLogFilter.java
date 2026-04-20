package com.mipt.sem2.hw4.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.regex.Pattern;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class AccessLogFilter extends OncePerRequestFilter {

    private static final Pattern JWT_PATTERN = Pattern.compile("(Bearer\\s+)([A-Za-z0-9\\-._~+/]+=*)");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(request, responseWrapper);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            int status = responseWrapper.getStatus();
            String traceId = MDC.get(TraceIdFilter.MDC_TRACE_ID_KEY);

            // Маскируем JWT в заголовке Authorization при логировании
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null) {
                authHeader = JWT_PATTERN.matcher(authHeader).replaceAll("$1***masked***");
            }

            log.info("HTTP {} {} -> status={} timeMs={} trace={} auth={}",
                request.getMethod(),
                request.getRequestURI(),
                status,
                duration,
                traceId,
                authHeader);

            responseWrapper.copyBodyToResponse();
        }
    }
}