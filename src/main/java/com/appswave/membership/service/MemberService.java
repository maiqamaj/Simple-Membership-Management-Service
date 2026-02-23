package com.appswave.membership.service;


import com.appswave.membership.enums.Gender;
import com.appswave.membership.enums.MembershipType;
import com.appswave.membership.enums.Persona;
import com.appswave.membership.web.dto.request.*;
import com.appswave.membership.web.dto.response.*;

import java.util.UUID;

public interface MemberService {
    MemberResponse createMember(CreateMemberRequest request);
    MemberResponse getMemberById(UUID id);
    PageResponse<MemberResponse> getAllMembers(int page, int size, String search,
                                               Gender gender, MembershipType membershipType, Persona persona);
    MemberResponse updateMember(UUID id, UpdateMemberRequest request);
    MemberResponse deleteMember(UUID id);

}
