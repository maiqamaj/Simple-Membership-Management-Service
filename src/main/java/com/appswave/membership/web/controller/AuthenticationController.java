package com.appswave.membership.web.controller;

import com.appswave.membership.exception.AuthenticationFailedException;
import com.appswave.membership.util.MessageKeys;
import com.appswave.membership.web.api.AuthenticationApi;
import com.appswave.membership.web.dto.request.LoginRequest;
import com.appswave.membership.web.dto.request.RegisterRequest;
import com.appswave.membership.web.dto.response.AuthenticationResponse;
import com.appswave.membership.service.AuthenticationService;
import com.appswave.membership.util.MessageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/authentication")
@RequiredArgsConstructor
public class AuthenticationController implements AuthenticationApi {

    private final AuthenticationService authenticationService;
    private final MessageUtil messageUtil;

    @Override
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthenticationResponse response = authenticationService.register(request);
            response.setMessage(messageUtil.getMessage(MessageKeys.AUTH_REGISTERED));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (AuthenticationFailedException e) {
            log.error("Failed to register user with email: {}", request.getEmail(), e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthenticationResponse response = authenticationService.login(request);
            response.setMessage(messageUtil.getMessage(MessageKeys.AUTH_LOGIN));
            return ResponseEntity.ok(response);
        } catch (AuthenticationFailedException e) {
            log.error("Failed to login user with email: {}", request.getEmail(), e);
            throw e;
        }
    }
}