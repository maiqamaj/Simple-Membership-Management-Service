package com.appswave.membership.exception;

import com.appswave.membership.util.MessageKeys;
import com.appswave.membership.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageUtil messageUtil;

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationFailedException(AuthenticationFailedException ex) {
        log.error("Authentication error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "success", false,
                        "message", messageUtil.getMessage(ex.getMessage())
                ));
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<Map<String, Object>> handleMemberException(AuthenticationFailedException ex) {
        log.error("Member error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "success", false,
                        "message", messageUtil.getMessage(ex.getMessage())
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("Unexpected error: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "success", false,
                        "message", ex.getMessage()
                ));
    }
}