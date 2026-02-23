package com.appswave.membership.web.dto.request;

import com.appswave.membership.enums.Gender;
import com.appswave.membership.enums.MembershipType;
import com.appswave.membership.enums.Persona;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateMemberRequest {
    private String firstName;
    private String lastName;

    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "{validation.mobile.invalid}")
    private String mobileNumber;

    private Gender gender;
    private MembershipType membershipType;
    private Persona persona;
}
