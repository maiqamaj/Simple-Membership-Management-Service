package com.appswave.membership.web.controller;
import com.appswave.membership.exception.MemberException;
import com.appswave.membership.service.MemberService;
import com.appswave.membership.util.MessageKeys;
import com.appswave.membership.util.MessageUtil;
import com.appswave.membership.web.api.MemberApi;
import com.appswave.membership.web.dto.request.*;
import com.appswave.membership.web.dto.response.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.appswave.membership.enums.*;

import java.util.UUID;


@Slf4j
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController implements MemberApi {

    private final MemberService memberService;
    private final MessageUtil messageUtil;

    @Override
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody CreateMemberRequest request) {
        try {
            MemberResponse response = memberService.createMember(request);
            response.setMessage(messageUtil.getMessage(MessageKeys.MEMBER_CREATED));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (MemberException e) {
            log.error("Failed to create member with email: {}", request.getEmail(), e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<PageResponse<MemberResponse>> getAllMembers(int page, int size, String search,
                                                        Gender gender, MembershipType membershipType, Persona persona) {
        try {
            PageResponse<MemberResponse> response = memberService.getAllMembers(page, size, search, gender, membershipType, persona);
            response.setMessage(messageUtil.getMessage(MessageKeys.MEMBER_LIST_FOUND));
            return ResponseEntity.ok(response);
        } catch (MemberException e) {
            log.error("Failed to retrieve members", e);
            throw e;
        }
    }


    @Override
    public ResponseEntity<MemberResponse> getMemberById(@PathVariable UUID id) {
        try {
            MemberResponse response = memberService.getMemberById(id);
            response.setMessage(messageUtil.getMessage(MessageKeys.MEMBER_FOUND));
            return ResponseEntity.ok(response);
        } catch (MemberException e) {
            log.error("Failed to retrieve member with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<MemberResponse> updateMember(@PathVariable UUID id,
                                                        @Valid @RequestBody UpdateMemberRequest request) {
        try {
            MemberResponse response = memberService.updateMember(id, request);
            response.setMessage(messageUtil.getMessage(MessageKeys.MEMBER_UPDATED));
            return ResponseEntity.ok(response);
        } catch (MemberException e) {
            log.error("Failed to update member with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable UUID id) {
        try {
            MemberResponse response = memberService.deleteMember(id);
            response.setMessage(messageUtil.getMessage(MessageKeys.MEMBER_DELETED));
            return ResponseEntity.ok(response);
        } catch (MemberException e) {
            log.error("Failed to delete member with id: {}", id, e);
            throw e;
        }
    }

}