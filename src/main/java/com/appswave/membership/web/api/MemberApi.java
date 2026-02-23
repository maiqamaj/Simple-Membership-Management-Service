package com.appswave.membership.web.api;

import com.appswave.membership.enums.*;
import com.appswave.membership.web.dto.request.*;
import com.appswave.membership.web.dto.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Members", description = "Endpoints for member management")
public interface MemberApi {

    @Operation(summary = "Create a new member", description = "Admin only — creates a new member.")
    @Parameters(@Parameter(in = ParameterIn.HEADER, name = "Accept-Language", required = true))
    @PostMapping( consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<MemberResponse> createMember(@Valid @RequestBody CreateMemberRequest request);

    @Operation(summary = "Get all members", description = "Admin only — returns paginated list of members.")
    @Parameters(@Parameter(in = ParameterIn.HEADER, name = "Accept-Language", required = true))
    @GetMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<PageResponse<MemberResponse>> getAllMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) MembershipType membershipType,
            @RequestParam(required = false) Persona persona);

    @Operation(summary = "Get member by ID", description = "Admin only — returns a single member.")
    @Parameters(@Parameter(in = ParameterIn.HEADER, name = "Accept-Language", required = true))
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<MemberResponse> getMemberById(@PathVariable UUID id);

    @Operation(summary = "Update member", description = "Admin only — updates a member.")
    @Parameters(@Parameter(in = ParameterIn.HEADER, name = "Accept-Language", required = true))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<MemberResponse> updateMember(@PathVariable UUID id, @Valid @RequestBody UpdateMemberRequest request);

    @Operation(summary = "Delete member", description = "Admin only — deletes a member.")
    @Parameters(@Parameter(in = ParameterIn.HEADER, name = "Accept-Language", required = true))
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<MemberResponse> deleteMember(@PathVariable UUID id);

   }