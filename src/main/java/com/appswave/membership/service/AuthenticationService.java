package com.appswave.membership.service;

import com.appswave.membership.web.dto.request.LoginRequest;
import com.appswave.membership.web.dto.request.RegisterRequest;
import com.appswave.membership.web.dto.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse login(LoginRequest request);
}
