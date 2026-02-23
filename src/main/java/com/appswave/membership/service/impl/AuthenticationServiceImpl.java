package com.appswave.membership.service.impl;

import com.appswave.membership.enums.Role;
import com.appswave.membership.exception.AuthenticationFailedException;
import com.appswave.membership.service.AuthenticationService;
import com.appswave.membership.util.JwtService;
import com.appswave.membership.util.MessageKeys;
import com.appswave.membership.web.dto.request.LoginRequest;
import com.appswave.membership.web.dto.request.RegisterRequest;
import com.appswave.membership.entity.User;
import com.appswave.membership.repository.UserRepository;
import com.appswave.membership.security.UserDetailsImpl;
import com.appswave.membership.web.dto.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthenticationFailedException(MessageKeys.ERROR_AITH_EMAIL);
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.USER)
                .build();

        userRepository.save(user);
        log.info("New user registered: {}", user.getEmail());

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name(), user.getId());
        return buildAuthenticationResponse(token, user);
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getEmail()).orElseThrow();
        String token = jwtService.generateToken( userDetails.getEmail(),
                user.getRole().name(), userDetails.getId());

        log.info("User logged in: {}", userDetails.getEmail());
        return buildAuthenticationResponse(token, user);
    }

    private AuthenticationResponse buildAuthenticationResponse(String token, User user) {
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(token);
        response.setTokenType("Bearer");
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setRole(user.getRole().name());
        return response;
    }
}
