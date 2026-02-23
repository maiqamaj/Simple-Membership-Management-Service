package com.appswave.membership.config;

import com.appswave.membership.util.JwtConfigProperties;
import com.appswave.membership.exception.AuthenticationFailedException;
import com.appswave.membership.util.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final JwtConfigProperties jwtConfigProperties;
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        return path.equals("/")
                || path.equals("/error")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/api/v1/authentication");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getTokenFromRequest(request);

            if (StringUtils.isEmpty(token)) {
                throw new AuthenticationFailedException(
                        String.format("%s header should contain a valid token", jwtConfigProperties.getHeader()));
            }

            String email = jwtService.extractEmail(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (jwtService.validateToken(token)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new AuthenticationFailedException("Invalid token");
            }

        } catch (Exception ex) {
            handleError(response, ex);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(jwtConfigProperties.getHeader());
        if (StringUtils.isNoneBlank(token) && token.startsWith(jwtConfigProperties.getPrefix())) {
            return token.replace(jwtConfigProperties.getPrefix(), "").trim();
        }
        return null;
    }

    private void handleError(HttpServletResponse response, Exception ex) {
        try {
            log.error("Authentication error: {}", ex.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getOutputStream(),
                    Map.of(
                            "success", false,
                            "message", ex.getMessage(),
                            "status", HttpStatus.UNAUTHORIZED.value()
                    ));
        } catch (Exception e) {
            log.error("Error while handling authentication exception: {}", e.getMessage());
        }
    }
}