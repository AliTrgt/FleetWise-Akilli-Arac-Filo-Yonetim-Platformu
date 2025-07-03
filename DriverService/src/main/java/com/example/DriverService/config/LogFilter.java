package com.example.DriverService.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Configuration
public class LogFilter extends OncePerRequestFilter {
    private static final Logger _logger = LoggerFactory.getLogger(LogFilter.class);
    public static final String REQUEST_ID = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put(REQUEST_ID,requestId);


        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);


        try {
            filterChain.doFilter(wrappedRequest,wrappedResponse);
        }finally {
            logRequest(wrappedRequest);
            logResponse(wrappedResponse);

            MDC.remove(requestId);
        }

    }

    private void logRequest(ContentCachingRequestWrapper request){
        String body = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);
        _logger.info("[REQUEST]: [METHOD]={}, [URI]={}, [BODY]={}",
                request.getMethod(),
                request.getRequestURI(),
                body);
    }

    private void logResponse(ContentCachingResponseWrapper response) throws IOException {
        byte[] respArray = response.getContentAsByteArray();
        String responseBody = new String(respArray, response.getCharacterEncoding());
        _logger.info("[RESPONSE]: [STATUS]={}, [BODY]={}", response.getStatus(), responseBody);

        response.copyBodyToResponse();
    }



}
