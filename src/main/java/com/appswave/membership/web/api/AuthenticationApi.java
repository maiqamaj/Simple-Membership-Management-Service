package com.appswave.membership.web.api;

import com.appswave.membership.web.dto.request.LoginRequest;
import com.appswave.membership.web.dto.request.RegisterRequest;
import com.appswave.membership.web.dto.response.AuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public interface AuthenticationApi {


    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account and returns a JWT token upon successful registration.",
            requestBody = @RequestBody(
                    description = "Registration details including name, email, and password",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterRequest.class)
                    )
            )
            ,
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "201",
                            description = "User registered successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Validation failed — missing or invalid fields",
                            content = @Content(mediaType = "application/json")
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "409",
                            description = "Email already registered",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @Parameters( @Parameter(in = ParameterIn.HEADER, name = "Accept-Language", required = true))
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request);


    @Operation(
            summary = "Login with existing credentials",
            description = "Authenticates a user with email and password and returns a JWT token.",
            requestBody = @RequestBody(
                    description = "Login credentials",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequest.class)
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Login successful",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Validation failed — missing or invalid fields",
                            content = @Content(mediaType = "application/json")
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "401",
                            description = "Invalid email or password",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @Parameters( @Parameter(in = ParameterIn.HEADER, name = "Accept-Language", required = true))
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request);
}
