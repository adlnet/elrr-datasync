package com.deloitte.elrr.datasync;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JSONRequestSizeLimitFilter extends OncePerRequestFilter {
    
    @Value("${json.max.size.limit}")
    private long maxSizeLimit;
    private long MAX_SIZE_LIMIT = maxSizeLimit;

    @Value("${mediaType.json}")
    private String mediaTypeJson;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (isApplicationJson(request) && request.getContentLengthLong() < MAX_SIZE_LIMIT) {
            filterChain.doFilter(request, response);
        }else {
            log.error("Request size exceeds the limit.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request size exceeds the limit.");
        }
    }

    private boolean isApplicationJson(HttpServletRequest httpRequest) {
        
        if (StringUtils.equals(mediaTypeJson, "off")) {
            return true;
        } else {
            return (MediaType.APPLICATION_JSON.isCompatibleWith(MediaType
                    .parseMediaType(httpRequest.getHeader(HttpHeaders.CONTENT_TYPE))));
        }
               
    }

}
