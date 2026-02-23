package com.appswave.membership.service.impl;


import com.appswave.membership.entity.Member;
import com.appswave.membership.enums.Gender;
import com.appswave.membership.enums.MembershipType;
import com.appswave.membership.enums.Persona;
import com.appswave.membership.exception.MemberException;
import com.appswave.membership.repository.MemberRepository;
import com.appswave.membership.service.MemberService;
import com.appswave.membership.web.dto.request.CreateMemberRequest;
import com.appswave.membership.web.dto.request.UpdateMemberRequest;
import com.appswave.membership.web.dto.response.MemberResponse;
import com.appswave.membership.web.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public MemberResponse createMember(CreateMemberRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new MemberException("error.member.email.exists");
        }
        Member member = Member.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .mobileNumber(request.getMobileNumber())
                .gender(request.getGender())
                .membershipType(request.getMembershipType())
                .persona(request.getPersona())
                .deleted(false)
                .build();

        member = memberRepository.save(member);
        log.info("Member created with id: {}", member.getId());
        return toResponse(member);
    }

    @Override
    public MemberResponse getMemberById(UUID id) {
        Member member = memberRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new MemberException("error.member.not.found"));
        return toResponse(member);
    }

    @Override
    public PageResponse<MemberResponse> getAllMembers(int page, int size, String search,
                                                      Gender gender, MembershipType membershipType, Persona persona) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Member> membersPage = memberRepository.findAllWithFilters(
                search, gender, membershipType, persona, pageable);

        PageResponse<MemberResponse> response = new PageResponse<>();
        response.setContent(membersPage.getContent().stream().map(this::toResponse).toList());
        response.setPage(membersPage.getNumber());
        response.setSize(membersPage.getSize());
        response.setTotalElements(membersPage.getTotalElements());
        response.setTotalPages(membersPage.getTotalPages());
        response.setLast(membersPage.isLast());
        return response;
    }

    @Override
    @Transactional
    public MemberResponse updateMember(UUID id, UpdateMemberRequest request) {
        Member member = memberRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new MemberException("error.member.not.found"));
        applyUpdates(member, request);
        return toResponse(memberRepository.save(member));
    }

    @Override
    @Transactional
    public MemberResponse deleteMember(UUID id) {
        Member member = memberRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new MemberException("error.member.not.found"));
        member.setDeleted(true);
        memberRepository.save(member);

        log.info("Member deleted: {}", id);

        return toResponse(member);
    }

    private void applyUpdates(Member member, UpdateMemberRequest request) {
        if (request.getFirstName() != null) member.setFirstName(request.getFirstName());
        if (request.getLastName() != null) member.setLastName(request.getLastName());
        if (request.getMobileNumber() != null) member.setMobileNumber(request.getMobileNumber());
        if (request.getGender() != null) member.setGender(request.getGender());
        if (request.getMembershipType() != null) member.setMembershipType(request.getMembershipType());
        if (request.getPersona() != null) member.setPersona(request.getPersona());
    }

    private MemberResponse toResponse(Member member) {
        MemberResponse response = new MemberResponse();
        response.setId(member.getId());
        response.setFirstName(member.getFirstName());
        response.setLastName(member.getLastName());
        response.setEmail(member.getEmail());
        response.setMobileNumber(member.getMobileNumber());
        response.setGender(member.getGender() != null ? member.getGender().name() : null);
        response.setMembershipType(member.getMembershipType() != null ? member.getMembershipType().name() : null);
        response.setPersona(member.getPersona() != null ? member.getPersona().name() : null);
        return response;
    }
}
